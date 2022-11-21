"use strict";

let userInput = document.getElementById("userName");
let roomInput = document.getElementById("roomName");
let msgInput = document.getElementById("msgText");
let msgOutputWindow = document.getElementById("msgWindow");
let displayUserInRoom = document.getElementById("userInRoom");
let currentRoom = null;


roomInput.addEventListener("keypress", handleKeyPressCB);
msgInput.addEventListener("keypress", handleMsgInputCB);


function handleKeyPressCB(event) {
    if(event.keyCode === 13){
        event.preventDefault();
        if((roomInput.value !== roomInput.value.toLowerCase()) || (roomInput.value ==="")){
            alert("Invalid room name, please enter a valid name with lowercase and without space");
            roomInput.select();
        }
        else if(userInput.value === ""){
            alert("Please enter an user name");
        }
        else if(wsOpen){
            let sendString = "join"+" "+userInput.value+" "+roomInput.value;
            ws.send(sendString);
            // if(roomInput.value !== currentRoom && currentRoom !==null){
            //     console.log(currentRoom);
            //     let leaveString = "leave"+" "+userInput.value+" "+currentRoom;
            //     ws.send(leaveString);
            // }
        }
        else{
            alert("room not found");
        }
    }

}

function handleMsgInputCB(event) {

    if(event.keyCode === 13){
        event.preventDefault();
        let sendString = userInput.value + " " + msgInput.value;
        if(wsOpen){
            ws.send(sendString);
            msgInput.value = "";
        }

    }
}

function handleMessageFromWsCB(event) {
    // console.log(event.data);
    let userData = JSON.parse(event.data);

    let d = new Date();
    let hour = d.getHours();
    let minute = d.getMinutes();
    let second = d.getSeconds();
    // chatBox.append(hour + ":" + minute + " " + data.user + ": " + data.message);
    // chatBox.append(br);

    if(userData.type === "join"){

        let msgDisplay = document.createElement("blockquote");
        let userDisplay = document.createElement("blockquote");
        userDisplay.textContent = userData.user;
        userDisplay.setAttribute("name", userData.user);
        currentRoom = userData.room;
        msgDisplay.textContent = "[ " + hour + ":" + minute + ":" + second + " ]  " + userData.user + " has joined the room ";
        msgDisplay.style.fontWeight = "bold";
        msgOutputWindow.appendChild(msgDisplay);
        displayUserInRoom.appendChild(userDisplay);
    }
    else if (userData.type ==="message"){

        let display = document.createElement("blockquote");
        display.textContent = "[ " + hour + ":" + minute + ":" + second + " ]  " + userData.user + " : " + userData.message;

        msgOutputWindow.appendChild(display);

    }
    else if(userData.type === "leave"){
        let msgDisplay = document.createElement("blockquote");

        msgDisplay.textContent = "[ " + hour + ":" + minute + ":" + second + " ]  " + userData.user + " has left the room";
        msgDisplay.style.fontWeight = "bold";
        msgOutputWindow.appendChild(msgDisplay);

        const userLeave = document.getElementsByName(userData.user);
            displayUserInRoom.removeChild(userLeave[0]);

    }
    displayUserInRoom.scrollTop = displayUserInRoom.scrollHeight;
    msgOutputWindow.scrollTop = msgOutputWindow.scrollHeight;

}

let wsOpen = false;
function handleConnectCB(event) {

    wsOpen = true;
    // ws.send("hello!!!!!");
    console.log("WSOPEN");

}

function handleCloseCB(event) {

    console.log("WSCLOSE");
}


let ws = new WebSocket("ws://localhost:8080");
ws.onopen = handleConnectCB;
ws.onmessage = handleMessageFromWsCB;
// ws.onclose = handleCloseCB;
