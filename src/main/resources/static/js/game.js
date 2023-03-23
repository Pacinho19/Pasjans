var stompClient = null;
var privateStompClient = null;

socket = new SockJS('/ws');
privateStompClient = Stomp.over(socket);
privateStompClient.connect({}, function (frame) {
    var gameId = document.getElementById('gameId').value;
    privateStompClient.subscribe('/reload-board/' + gameId, function (result) {
        updateBoard();
    });
});

stompClient = Stomp.over(socket);

function updateBoard() {
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState == XMLHttpRequest.DONE) {
            $("#board").replaceWith(xhr.responseText);
            addStackAction();
        }
    }
    xhr.open('GET', "/pasjans/game/" + document.getElementById("gameId").value + "/board/reload", true);
    xhr.send(null);
}

function addStackAction(){
    element = document.getElementsByClassName('stackCard')[0];

    if(element.nodeName == "SPAN"){
        element.addEventListener('click', function (event) {
            stackAction();
        });
    }else{
      $('h1').click(function(){
          stackAction();
      });
    }
}


function stackAction() {

 var xhr = new XMLHttpRequest();
    var url = '/pasjans/game/' + document.getElementById("gameId").value + '/stack/next';
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () { };
    xhr.send();

}

function clickCard(cardDto, cardAction){
    if(!cardAction) return;

    var xhr = new XMLHttpRequest();
    var url = '/pasjans/game/' + document.getElementById("gameId").value + '/card/group/add';
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () { };
    var data = JSON.stringify(cardDto);

    console.log(data);
    xhr.send(data);

}