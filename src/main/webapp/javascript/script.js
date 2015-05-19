	function getMessage()
	{
		var message = document.getElementById('ChatText').value;
		return message;
	}

	var uniqueId = function() {
		
		var date = Date.now();
		var random = Math.random() * Math.random();
		return Math.floor(date * random).toString();
	};

	var theMessage = function(t, name) {
		return {
			text:t,
			author: name,
			id: uniqueId(),
			deleted: "false",
		};
	};

	var appState = {
		mainUrl : 'WebChat',
		msgList: [],
		token : 'TN11EN',
		currentUser: ''
	};

	function run(){
		var appContainer = document.getElementsByClassName('BlockMessageEnter')[0];
	    appContainer.addEventListener('click', delegateEvent);
	    var loginContainer = document.getElementsByClassName('BlockLogin')[0];
		loginContainer.addEventListener('click', delegateEvent);
		var messageContainer = document.getElementsByClassName('BlockChatting')[0];
		messageContainer.addEventListener('click', delegateEvent);
		restores();
		var user = restoreCurrentUser();
		if(user!=null)
		{
			appState.currentUser = user;
			addUser(user);
		}
	}

	function createAllMsg(msg) {
		for(var i = 0; i < msg.length; i++)
		{
			addRestoringInformation(msg[i]);
		}
	}


	function delegateEvent(evtObj) {
	if (evtObj.type === 'click'  && evtObj.target.classList.contains('ButtonSend') && document.getElementsByClassName('Select').length == 0) 
		addMessage();
			
	if (evtObj.type === 'click' && evtObj.target.classList.contains('ButtonLogin') && document.getElementsByClassName('Select').length == 0)
			addUser(getUserName());
	if (evtObj.type === 'click' && evtObj.target.classList.contains('ChangeLogin') && document.getElementsByClassName('Select').length == 0)
			changeUserName();
		var container = document.getElementsByClassName('Select');
		if (evtObj.type === 'click' && evtObj.target.classList.contains('ButtonLogin') && container.length>0) 
			changeUserNameFinal();
			
		if (evtObj.type === 'click' && evtObj.target.classList.contains('Delete'))
		{
			var label = evtObj.target.parentElement;
		    setMarker(label);	
			deleteMessageServer(getMessageId());
		}
		if (evtObj.type === 'click' && evtObj.target.classList.contains('Change'))
		{
			var label = evtObj.target.parentElement;
		    setMarker(label);	
			editMessage();
		}
		if (evtObj.type === 'click' && evtObj.target.classList.contains('ButtonSend') && container.length>0)
			editMessageServer(getMessageId(), appState.currentUser + ": " + getMessage());
	}

	function createMessage(text, id) {
		var divItem = document.createElement('div');
		divItem.innerHTML = '<div id=' + id+'><button class="Delete"></button><button class="Change"></button>'+text+'</div>';
		return divItem;
	}

	
	function addMessage() {   
		var name = appState.currentUser;
	    var list = appState.msgList;
		if(getMessage().length!=0 )
		{
		
		var chat = document.getElementsByClassName('BlockChatting')[0];
		var newMsg = theMessage(name+": "+getMessage(), name);
		list.push(newMsg);
		var message = createMessage(name+": "+getMessage(), newMsg.id);
		document.getElementById('ChatText').value='';
		addMessageServer(newMsg);
		}
	}

	function addMessageServer(message) {
		post(appState.mainUrl, JSON.stringify(message), function(){
			console.log("POST IS OK")
		});
	}

	function createUser(text)
	{
		var divItem = document.createElement('div');
		appState.currentUser = text;
		divItem.className = 'User';
		divItem.appendChild(document.createTextNode(text));
		storeCurrentUser(appState.currentUser);
		return divItem;
	}

	function getUserName()
	{
		var user = document.getElementById('TextLogin').value;
		document.getElementById('TextLogin').value = '';
		return user;
	}

	function addUser(text)
	{
		var users = document.getElementsByClassName('BlockUser')[0];
		var element = document.getElementById('Enter');
		var user = createUser(text);
		if(element!=null)
		element.remove();
	    var checkUser = document.getElementsByClassName('User');
	    if(checkUser.length == 0)
		{
		users.appendChild(user);
		document.getElementsByClassName('Interlocutor')[0].innerHTML = 'Anonymus';
		}
	    else alert("Вы уже вошли!");
		
	}

	function changeUserName()
	{
		var divItem = document.getElementsByClassName('User')[0];
		document.getElementById('TextLogin').value = divItem.firstChild.nodeValue;
		divItem.classList.add('Select');
	}

	function changeUserNameFinal()
	{
		var user = document.getElementsByClassName('Select');
		appState.currentUser = getUserName();
		user[0].innerHTML = '<div class = "User">'+appState.currentUser+"</div>";
		storeCurrentUser(appState.currentUser);
		setMarker(user[0]);
	}

	function setMarker(label)
	{
		if(label.classList.contains('Select'))
			label.classList.remove('Select');
		else label.classList.add('Select');
	}
	
	function getMessageId() {
		
		var messages = document.getElementsByClassName('Select');
		var id = messages[0].getAttribute('id');
		return id;
	}
	
	function deleteMessage(id) {
	
		var msg = document.getElementById(id);
		
		for(var i = 0; i < appState.msgList.length; i++){
			if(appState.msgList[i].id != id)
				continue;
		    if(appState.msgList[i].deleted == "true"){
				  messages[0].classList.remove('Select');
			      alert("Сообщение удалено!");
			      return;
		    }	
			
		    if(appState.msgList[i].deleted == "false"){
		    msg.innerHTML = '<div id=' + appState.msgList[i].id+'><button class="Delete"></button><button class="Change"></button>'
			+appState.msgList[i].author+': Message was deleted</div>';
		    msg.classList.remove('Select');
			appState.msgList[i].deleted = "true";
		    }
		else{
		appState.msgList.splice(i,1);
		msg.remove();
		}
		}
	}

	function deleteMessageServer(id)
	{
		var index = {
			id: id
		}
		deletee(appState.mainUrl, JSON.stringify(index), function(){
			console.log("DELETE is OK")
		});
	}

	function editMessage()
	{
		var messages = document.getElementsByClassName('Select');
		var id = messages[0].getAttribute('id');
		for(var i = 0; i < appState.msgList.length; i++){
			if(appState.msgList[i].id == id){
				if(appState.msgList[i].deleted == "true"){
				  messages[0].classList.remove('Select');
			      alert("Сообщение удалено!");
			      return;
		        }
			}
		}
		var str = messages[0].innerHTML;
		var pos = str.indexOf(":")+1;
		var text = str.substring(pos);
	    document.getElementById('ChatText').value = text;
	   
	}

	function nextEditMessage(id, text){
		
		var msg = document.getElementById(id);
		
		for(var i = 0; i < appState.msgList.length; i++){
			if(appState.msgList[i].id != id)
				continue;
			
			appState.msgList[i].text = text;
			msg.innerHTML = '<div id=' + appState.msgList[i].id+'><button class="Delete"></button><button class="Change"></button>'+appState.msgList[i].text+'</div>';
		}
		document.getElementById('ChatText').value='';
		msg.classList.remove('Select');
	}

	function editMessageServer(id, newMsg){
		
		var object = {
			text: newMsg,
			id: id
		}
		put(appState.mainUrl, JSON.stringify(object), function(){
			console.log("PUT IS OK")
			});
	}


	function changeIndicatorOn(){
		var index = document.getElementById('Indicator');
		index.setAttribute('src','http://savepic.su/5321198.png');
	}

	function changeIndicatorOff(){
		var index = document.getElementById('Indicator');
		index.setAttribute('src','http://savepic.su/5187513.png');
	}

	function storeCurrentUser(user){
		if(typeof(Storage) == "undefined") {
			alert('localStorage is not accessible');
			return;
		}

		localStorage.setItem("UserName", JSON.stringify(user));
	}

	function restoreCurrentUser() {
		if(typeof(Storage) == "undefined") {
			alert('localStorage is not accessible');
			return;
		}

		var item = localStorage.getItem("UserName");

		return item && JSON.parse(item);
		}

	function addRestoringInformation(msg)
	{
		var list = appState.msgList;
		var chat = document.getElementsByClassName('BlockChatting')[0];
		chat.appendChild(createMessage(msg.text, msg.id));
		list.push(msg);
	}


	function get(url, continueWith, continueWithError) {
		ajax('GET', url, null, continueWith, continueWithError);
	}

	function post(url, data, continueWith, continueWithError) {
		ajax('POST', url, data, continueWith, continueWithError);	
	}

	function put(url, data, continueWith, continueWithError) {
		ajax('PUT', url, data, continueWith, continueWithError);	
	}

	function deletee(url, data, continueWith, continueWithError) {
		ajax('DELETE', url, data, continueWith, continueWithError);	
	}

	function ajax(method, url, data, continueWith, continueWithError) {
		var xhr = new XMLHttpRequest();
		continueWithError = continueWithError || defaultErrorHandler;
		xhr.open(method || 'GET', url, true);

		xhr.onload = function () {
			if (xhr.readyState !== 4)
				return;
			
			if( xhr.status == 304 ){
				console.log("not modified");
				}
			if(xhr.status != 200 ) {
				continueWithError('Error on the server side, response ' + xhr.status);
				return;
			}

			if(isError(xhr.responseText) ) {
				continueWithError('Error on the server side, response ' + xhr.responseText);
			    changeIndicatorOff();
				return;
			}

			continueWith(xhr.responseText);
		};   

	    xhr.ontimeout = function () {
	    	continueWithError('Server timed out !');
			changeIndicatorOff();
	    }

	    xhr.onerror = function (e) {
	    	var errMsg = 'Server connection error !\n'+
	    	'\n' +
	    	'Check if \n'+
	    	'- server is active\n'+
	    	'- server sends header "Access-Control-Allow-Origin:*"';
			changeIndicatorOff();
	    };

	    xhr.send(data);
		changeIndicatorOn();
	}

	function restores(continueWith) {
		function poll() {
		var url = appState.mainUrl + '?token=' + appState.token;

		get(url, function(responseText) {
			console.assert(responseText != null);

			var response = JSON.parse(responseText);

			appState.token = response.token;
			var messages = response.messages;
			
			for (var i = 0; i < messages.length; i++) {
				
				if (messages[i].request == "POST") {
					addRestoringInformation(messages[i]);
				}
				else if (messages[i].request == "DELETE") {
					deleteMessage(messages[i].id);
					
				} else if (messages[i].request == "PUT") {
					nextEditMessage(messages[i].id,messages[i].text);
				}
			}
			
			setTimeout(poll, 1000);
		}, function(error) {
			defaultErrorHandler(error);
			setTimeout(poll, 1000);
		});
	}

	poll();
}

	function defaultErrorHandler(message) {
		console.error(message);
	}

	function isError(text) {
		if(text == "")
			return false;
		
		try {
			var obj = JSON.parse(text);
		} catch(ex) {
			return true;
		}

		return !!obj.error;
	}
