# ParseProcessorクラスの役割

## 現状の問題点

### ParseContextインスタンスをたらいまわす記述が冗長である

次のコードはParseProcessorクラスを使わずにCSVのダブルクォート文字列を<br>
パースするコードの例である。<br>

```java:例1

       ParserContext<Character> sentAroundContext;
        
        sentAroundContext = parse(context, Matchers.character(enclosedCharacter));
        if(sentAroundContext.isFailure()) return sentAroundContext.map(c -> String.valueOf(c));
        
        StringBuilder sb = new StringBuilder();
        do {
            sentAroundContext = parse(sentAroundContext, Matchers.characterNot(enclosedCharacter));
            if(sentAroundContext.isFailure()) break;
            
            ParserContext<Character> backContext = sentAroundContext;
            char contentChar = sentAroundContext.getValue();
            if(sentAroundContext.getValue() == escapeCharacter) {
                sentAroundContext = parse(sentAroundContext, Matchers.character(escapeCharacter));
                if(sentAroundContext.isSuccess()) {
                    contentChar = escapeCharacter;
                } else {
                    sentAroundContext = backContext;
                    sentAroundContext = parse(sentAroundContext, Matchers.character(enclosedCharacter));
                    if(sentAroundContext.isSuccess()) {
                        contentChar = enclosedCharacter;
                    }
                    else {
                        sentAroundContext = backContext;
                    }
                }
            }           
            sb.append(contentChar);
        } while(sentAroundContext.isSuccess());
        
        sentAroundContext = parse(sentAroundContext, Matchers.character(enclosedCharacter));
        if(sentAroundContext.isFailure()) return sentAroundContext.map(c -> String.valueOf(c));;
        
        String matchedPart = sb.toString();
        
        return new ParserContext<>(
                sentAroundContext.getTarget(),
                sentAroundContext.getProvider(),
                matchedPart,
                ParseResult.SUCCESS
        );

```

`ParserContext<Character> sentAroundContext`をparseメソッドに渡し、結果を<br>
`ParserContext<Character> sentAroundContext`へ再代入し、またparseメソッドに渡して…<br>
とParseContextオブジェクトをたらいまわしながら処理を記述している。<br>

このたらいまわしはパースする際、最初から最後まで明示的に行わなければならない。<br>
これらを毎回記述しないようにしたい。<br>


### パースの結果に応じて、パース方法を分けていくとif文の入れ子が深くなる

ダブルクォート文字列にて、エスケープシーケンスの処理を例1から以下に<br>
抜粋する。<br>

```java:例2

            ParserContext<Character> backContext = sentAroundContext;
            char contentChar = sentAroundContext.getValue();
            if(sentAroundContext.getValue() == escapeCharacter) {
                sentAroundContext = parse(sentAroundContext, Matchers.character(escapeCharacter));
                if(sentAroundContext.isSuccess()) {
                    contentChar = escapeCharacter;
                } else {
                    sentAroundContext = backContext;
                    sentAroundContext = parse(sentAroundContext, Matchers.character(enclosedCharacter));
                    if(sentAroundContext.isSuccess()) {
                        contentChar = enclosedCharacter;
                    }
                    else {
                        sentAroundContext = backContext;
                    }
                }
            } 

```

例2のソースではパースしたダブルクォート以外の文字が\だった場合、
その次の文字が\なのかチェックし、そうでなければ"なのかチェックする。
そして、どちらでもなければ、ParseContextをパース前に戻している。

このようにパースの結果に応じてパース処理を分け、さらにそのパースの結果でも分けてと
再帰的に繰り返していくとif文の入れ子はどんどん深くなりソースが見づらくなっていく。
これを解消したい。

また、このようなパースがすべて失敗した場合、
ParseContextをパース前に戻すのもよくあるパターンなので
暗黙的に行いたい。

## 対応策

```java

        ParseContext<Character> sentAroundContext;
        
        sentAroundContext = parse(context, Matchers.character(enclosedCharacter));
        if(sentAroundContext.isFailure()) return sentAroundContext.map(c -> String.valueOf(c));
        
        StringBuilder sb = new StringBuilder();
        do {          
            ParseProcessor<Character,Character> parseProcessor
                = ParseProcessor
                    .begin(sentAroundContext, Character.class)
                    .parse(Matchers.characterNot(enclosedCharacter))
                        .setReturnIfSuccess(ParseContext<Character>::getValue)
                        .continueIfSuccess()
                        .continueIf(ctx -> ctx.getValue()==escapeCharacter)
                    .parse(Matchers.character(escapeCharacter))
                        .setReturnIfSuccess(escapeCharacter)
                        .continueIfFailure()
                        .backContext()
                    .parse(Matchers.character(enclosedCharacter))
                        .setReturnIfSuccess(enclosedCharacter)
                    .setReturnIfFailure((Character)null)
                    .end();
            
            sentAroundContext = parseProcessor.returnContext();
            if(sentAroundContext.isFailure()) break;
            
            sb.append(parseProcessor.returnValue());
        } while(sentAroundContext.isSuccess());
        
        sentAroundContext = parse(sentAroundContext, Matchers.character(enclosedCharacter));
        if(sentAroundContext.isFailure()) return sentAroundContext.map(c -> String.valueOf(c));;
        
        String matchedPart = sb.toString();
        
        return new ParseContext<>(
                sentAroundContext.getTarget(),
                sentAroundContext.getProvider(),
                matchedPart,
                ParseResult.SUCCESS,
                ""
        );
    }

```

ParseProcessorクラス

上記の問題点を次のように解決する。

### ParseContextインスタンスをたらいまわす記述が冗長である

・ParseProcessorクラスは内部にParseContextインスタンスを持ち、
  メソッドチェーンでParseProcessorクラスのインスタンスをたらいまわししながら
  書き換えていくことで、ParseContextのたらいまわしを暗黙的に処理することができ
  記述が不要となる。

```

```

### パースの結果に応じて、パース方法を分けていくとif文の入れ子が深くなる

・パースの結果に応じて、次の行以降のメソッドチェーンを実行するか
  パースを終了して戻り値を返すかを選べるようにする。
  
・次の行を実行するか、実行せず終了するかという2択をメソッドチェーンで
  一元的に表現しているので、if文の入れ子が発生せず階層が深くならない。


ParseProcessor.begin

・ParseProcessorでたらいまわしするParseContextを最初に指定する。
・2番目にParseProcessorの最終的な戻り値の型を指定する
・beginメソッドはParseProcessorクラスのstaticメソッドで
  戻り値にParseProcessorインスタンスを返す。

ParseProcessorは

