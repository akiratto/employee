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

ParseProcessorクラスは上記の問題点を次のように解決する。

### ParseContextインスタンスをたらいまわす記述が冗長である

・ParseProcessorクラスは内部にParseContextインスタンスを持ち、
  メソッドチェーンでParseProcessorクラスのインスタンスをたらいまわししながら
  書き換えていくことで、ParseContextのたらいまわしを暗黙的に処理することができ
  記述が不要となる。

例）
```

            ParseProcessor<String,String> parseProcessor  //[1]
                = ParseProcessor
                    .begin(new ParseContext<String,String>("aiueo,kakikukeko,sasisuseso")) //[2]
                    .parse(Matchers.string("aiueo,))        //[3]
                    .parse(Matchers.string("kakikukeko,"))  //[4]
                    .parse(Matchers.string("sasisuseso"))   //[5]
                    .end();   //[6]
            
            //[7]
            System.out.println("result=" + parsePrcessor.returnContext().getResult());
            System.out.println("input=" + parseProcessor.returnContext().getInput());
            System.out.println("value=" + parseProcessor.returnContext().getValue());

```
[1] ParseProcessorの1番目の型はParseContextの入力の型、2番目の型はParseContextの値の型を指定する。
    今回の例ではどちらもString型を指定する。

[2] beginメソッドでたらいまわしする最初のParseContextを指定する。
    例では入力に"aiueo,kakikukeko,sasisuseso"を持ち、値はnullを持つParseContextを指定している。
    この指定以降、ParseProcessorの終了までParseContextを明示的に指定することはない。
    このメソッドでParseProcessorの状態は"開始"となる。

[3] [2]で指定したParseContextで指定された入力"aiueo,kakikukeko,sasisuseso"を
    "aiueo,"の文字列でパースする。
    パースの結果は成功、ParseContextの入力は消費されて"kakikukeko,sasisuseso"となり、
    値に"aiueo,"が設定されて、次の行に渡される。

[4] [3]で処理したParseContextの入力"kakikukeko,sasisuseso"を"kakikukeko,"の文字列で
    パースする。
　　パースの結果は成功、ParseContextの入力は消費されて"sasisuseso"となり、
    値に"kakikukeko,"が設定されて、次の行に渡される。

[5] [4]で処理したParseContextの入力"sasisuseso"を"sasisuseso,"の文字列で
    パースする。
　　パースの結果は成功、ParseContextの入力は消費されて""となり、
    値に"sasisuseso"が設定されて、次の行に渡される。

[6] パース処理を終了して、[5]で処理したParseContextを保持した
    ParseProcessorインスタンスを取得する。
    ParseProcessorの状態は"終了"となり、ParseContextを取得するreturnContextメソッドが
    使用可能になる。
    
[7] ParseContextの結果、入力、値を表示する
    結果はSUCCESS、
    入力は""、
    値は"sasisuseso"となる。



### パースの結果に応じて、パース方法を分けていくとif文の入れ子が深くなる

・パースの結果に応じて、次の行以降のメソッドチェーンを実行するか
  パースを終了して戻り値を返すかを選べるようにする。
  
・次の行を実行するか、実行せず終了するかという2択をメソッドチェーンで
  一元的に表現しているので、if文の入れ子が発生せず階層が深くならない。

```例）

            final Character enclosedCharacter = '"';
            final Character escapeCharacter   = '\';
            ParseProcessor<String,Character> parseProcessor  //[1]
                = ParseProcessor
                    .begin(sentAroundContext, Character.class)       //[2]
                    .parse(Matchers.characterNot(enclosedCharacter)) //[3]
                        .setReturnIfSuccess(ParseContext<String,Character>::getValue)  //[4]
                        .continueIfSuccess()  //[5]
                        .continueIf(ctx -> ctx.getValue()==escapeCharacter) //[6]
                    .parse(Matchers.character(escapeCharacter)) //[7]
                        .setReturnIfSuccess(escapeCharacter) //[8]
                        .continueIfFailure() //[9]
                        .backContext() //[10]
                    .parse(Matchers.character(enclosedCharacter)) //[11]
                        .setReturnIfSuccess(enclosedCharacter) //[12]
                    .setReturnIfFailure((Character)null) //[13]
                    .end(); //[14]

```

[1] 


ParseProcessor.begin

・ParseProcessorでたらいまわしするParseContextを最初に指定する。
・2番目にParseProcessorの最終的な戻り値の型を指定する
・beginメソッドはParseProcessorクラスのstaticメソッドで
  戻り値にParseProcessorインスタンスを返す。

ParseProcessorは



Matcherについて

Matcherは次の役割を持つ。

・指定されたパターンに入力がマッチするか判定し、
  マッチすれば、ParseContextの結果にSUCCESSを設定し、ParseContextの値にマッチした部分を設定し、
   ParseContextの入力からマッチした部分を除去する。
　マッチしなければ、ParseContextの結果にFAILUREを設定し、値にはnullを設定、入力はマッチ前と同じとなる。

・ParseContextの入力の文字数が、指定されたパターンに必要な文字数に満たない場合、
  サプライヤーへ入力に追加する文字列を要求する。
  この要求は必要な文字数に達するまで繰り返されるが、サプライヤーの供給が切れた場合は
  マッチしない場合と同じで、ParseContextの結果にFAILURE、値にnull、入力はマッチ前にサプライヤーが供給した分を追加したものになる。

・供給でパターンに必要な文字数を満たした後は、通常通り、指定されたパターンにマッチするかの判定が行われる。

・Matcherは２つの型パラメータを持ち、１つはParseContextの入力の型、2つ目はParseContextの値の型となる。


