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

function addStackAction() {
    element = document.getElementsByClassName('stackCard')[0];

    if (element.nodeName == "SPAN") {
        element.addEventListener('click', function (event) {
            stackAction();
        });
    } else {
        $('h1').click(function () {
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

function clickCard(cardDto, cardAction) {
    if (!cardAction) return;

    var xhr = new XMLHttpRequest();
    var url = '/pasjans/game/' + document.getElementById("gameId").value + '/card/group/add';
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () { };
    var data = JSON.stringify(cardDto);

    console.log(data);
    xhr.send(data);
}

let selectedCard;
function selectCard(cardDto, columnNumber) {
    if (cardDto == null && selectedCard != null) {
        moveCardDto = { firstCard: selectedCard, secondCard: null, columnNumber : columnNumber };
        selectedCard = null;
        sendMoveCardRequest(moveCardDto);
        return;
    }

    cardName = cardDto != null ? cardDto.suit + '_' + cardDto.rank : null;
    selectedCardName = selectedCard != null ? (selectedCard.suit + '_' + selectedCard.rank) : null;
    cardElement = document.getElementById(cardName);

    if (selectedCard != null && selectedCardName !== cardName) {
        moveCardDto = { firstCard: selectedCard, secondCard: cardDto, columnNumber : null };
        selectedCard = null;
        sendMoveCardRequest(moveCardDto);
        return;
    }

    if (selectedCardName === cardName) {
        selectedCard = null;
        cardElement.style.border = "2px solid black";
    } else {
        selectedCard = cardDto;
        cardElement.style.border = "thick solid #0000FF";
    }
}

function sendMoveCardRequest(moveCardDto) {
    var xhr = new XMLHttpRequest();
    var url = '/pasjans/game/' + document.getElementById("gameId").value + '/card/move';
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () { };
    var data = JSON.stringify(moveCardDto);

    console.log(data);
    xhr.send(data);
}
