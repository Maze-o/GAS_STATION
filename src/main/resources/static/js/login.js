var loginBtnElem = document.querySelector('#loginBtn')

if (loginBtnElem) {
	var formElem = document.querySelector('#form')
	var userIdElem = formElem.userId
	var userPwElem = formElem.userPw

	function ajax() {
		if (userIdElem.value === '') {
			alert('아이디를 입력해 주세요')
			return
		} else if (userPwElem.value === '') {
			alert('비밀번호를 입력해 주세요.')
			return
		}

		var param = {
			userEmail: userIdElem.value,
			userPw: userPwElem.value
		}

		fetch('/login', {
			method: 'post',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify(param)
		}).then(function(res) {
			return res.json()
		}).then(function(myJson) {
			proc(myJson)
		})

	}

	function proc(myJson) {
		switch (myJson.result) {
			case 1:
				alert('환영합니다!')
				history.go(-1)
				return
			case 2:
				alert('아이디를 확인해 주세요')
				return
			case 3:
				alert('비밀번호를 확인해 주세요')
				return

		}
	}

	loginBtnElem.addEventListener('click', ajax)
}