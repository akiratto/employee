var dialog_title;
var dialog_frame;
function dialog_show_success(data) {
    if(data.status === 'success') {
        dialog_init();
        dialog_frame.style.display = 'block';
    }
}

function dialog_close_success(data) {
    if(data.status === 'success') {
        dialog_frame.style.display = 'none';
    }
}

function dialog_init() {
    dialog_title = document.getElementsByClassName("dialog_title")[0];
    dialog_frame = dialog_title.parentNode;

    dialog_title.onmousedown = function(event) {
        let shiftX = event.clientX - dialog_frame.getBoundingClientRect().left;
        let shiftY = event.clientY - dialog_frame.getBoundingClientRect().top;

        moveAt(event.pageX, event.pageY);

        function moveAt(pageX, pageY) {
            dialog_frame.style.left = pageX - shiftX + 'px';
            dialog_frame.style.top = pageY - shiftY + 'px'; 
        }

        function onMouseMove(event) {
            moveAt(event.pageX, event.pageY);
        }

        document.addEventListener('mousemove', onMouseMove);

        dialog_title.onmouseup = function() {
            document.removeEventListener('mousemove', onMouseMove);
            dialog_title.onmouseup = null;
        };
    };

    dialog_title.ondragstart = function() {
       return false;
    };
};

dialog_init();