function passwordRequest() {
	LoginSignup.passwordRequest(generateFormData("#newAccountScreen"))
	.then(()=>{
		alert("Kijk op je e-mail.");
		$("#loginOptions").hide();
	}).catch(error=>{
		console.log(error);
		alert(error)
	});
}
function load() {
	return LoginSignup.getLoggedinUser().catch(message=>{
		$('.afterLogin').hide();
		if(message==403 || message=="niet ingelogd") $('#notLoggedIn').show();
		else {
			console.log(message);
			alert(message);
		}
		return Promise.reject(403);
	}).then(user=> {
		$('.afterLogin').show()
		$('#userMenuName').text(user.name);
		$('#userMenuPicture').attr('src','/rest/media/'+user.profilePicture);
		return user
	})
}
function savePassword() {
	LoginSignup.savePassword(generateFormData("#newPasswordScreen"))
		.catch(error=>{
			console.log(error);
			alert(error)
		})
		.then(()=> {
			$('#newPasswordScreen').hide();
			$('#loginOptions').show();
			alert("Wachtwoord ingesteld.");
			window.location.hash = "";
		}).then(load)
}
function login() {
	LoginSignup.login(generateFormData("#loginScreen"))
	.catch(error=>{
		console.log(error);
		alert(error);
		return Promise.reject(error);
	}).then(()=> {
		$('#notLoggedIn').hide();
		load().then(handleHash);
	})
}
function logout() {
	return unsubscribe()
		.then(LoginSignup.logout)
		.then(()=>{
			$('#userMenuName').text("");
			$('#userMenu').hide();
			$('#settings').hide();
			logoutPost();
		}).then(load)
}