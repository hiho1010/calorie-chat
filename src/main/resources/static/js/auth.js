/*  로그인 & 회원가입 폼을 JSON API에 연결
    - POST /api/v1/users/login    { email, password }
    - POST /api/v1/users/register { email, nickname, password, confirm }
*/

function sendJson(form, url, mapper, onSuccess, onError) {
  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const payload = mapper();              // 입력 → JSON

    try {
      const res = await fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      if (res.ok) {
        onSuccess();
      } else {
        const err = await res.json().catch(() => ({}));
        onError(err.message || `요청 실패 (${res.status})`);
      }
    } catch (ex) {
      console.error(ex);
      onError('서버와 통신할 수 없습니다.');
    }
  });
}

document.addEventListener('DOMContentLoaded', () => {

  /* ── 로그인 ─────────────────────── */
  const loginForm = document.getElementById('loginForm');
  if (loginForm) {
    const msg = document.getElementById('loginMsg');
    const showErr = (m) => { msg.textContent = m; msg.classList.remove('d-none'); };

    sendJson(
        loginForm,
        '/api/v1/users/login',
        () => ({
          email:    loginForm.email.value,
          password: loginForm.password.value
        }),
        () => { window.location.href = '/'; },
        showErr
    );
  }

  /* ── 회원가입 ──────────────────── */
  const signupForm = document.getElementById('signupForm');
  if (signupForm) {
    const msg = document.getElementById('signupMsg');
    const showErr = (m) => { msg.textContent = m; msg.classList.remove('d-none'); };

    sendJson(
        signupForm,
        '/api/v1/users/register',
        () => ({
          email:    signupForm.email.value,
          nickname: signupForm.nickname.value,
          password: signupForm.password.value,
          confirm:  signupForm.confirm.value
        }),
        () => {
          alert('회원가입이 완료되었습니다! 로그인해 주세요.');
          window.location.href = '/login';
        },
        showErr
    );
  }
});