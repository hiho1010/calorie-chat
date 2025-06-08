/* static/js/auth.js */
/*  Vanilla JS helper to send JSON login & register requests
    Spring Security CSRF 토큰은 숨은 input에서 읽어 Header에 실어 보냅니다. */

function sendJson(form, url, mapper) {
  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const csrfInput = form.querySelector('input[name=\"_csrf\"]');
    const csrfToken = csrfInput ? csrfInput.value : '';

    const payload = mapper();

    try {
      const res = await fetch(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'X-CSRF-TOKEN': csrfToken
        },
        body: JSON.stringify(payload)
      });

      if (res.ok) {
        if (url === '/login') {
          // 로그인 성공 → 홈
          window.location.href = '/';
        } else {
          alert('회원가입이 완료되었습니다! 로그인해 주세요.');
          window.location.href = '/login';
        }
      } else {
        const err = await res.json().catch(() => ({}));
        alert(err.message || '요청에 실패했습니다.');
      }
    } catch (ex) {
      console.error(ex);
      alert('서버와 통신할 수 없습니다.');
    }
  });
}

document.addEventListener('DOMContentLoaded', () => {
  const loginForm = document.getElementById('loginForm');
  if (loginForm) {
    sendJson(loginForm, '/login', () => ({
      username: loginForm.username.value,
      password: loginForm.password.value
    }));
  }

  const signupForm = document.getElementById('signupForm');
  if (signupForm) {
    sendJson(signupForm, '/register', () => ({
      email:    signupForm.email.value,
      nickname: signupForm.nickname.value,
      password: signupForm.password.value,
      confirm:  signupForm.confirm.value
    }));
  }
});