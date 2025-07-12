import { useContext, useState } from 'react';
import './index.css';
import { authenticate } from '../../utils';
import { MainContext } from '../../context';
import { useNavigate } from 'react-router-dom';

function SignIn() {
  const navigate = useNavigate();
  const [error, setError] = useState(false);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const { userLogged, setUserLogged } = useContext(MainContext);

  const handleEmail = (e) => setEmail(e.target.value);
  const handlePassword = (e) => setPassword(e.target.value);
  const handleSubmit = async (e) => {
    e.preventDefault();
    await authenticate(email, password, setUserLogged, setError, userLogged);
    navigate('/5');
  };

  return (
    <div className='signIn-container'>
      <div>
        <h1>Sign In</h1>
        <form onSubmit={handleSubmit}>
          <div>
            <span>
              <label htmlFor='email'>Email</label>
              <input
                type='text'
                name='Sapo'
                id='email'
                placeholder='Email'
                onChange={handleEmail}
                autoComplete='user-name'
                value={email}
              />
            </span>
            <span>
              <label htmlFor='password'>Password</label>
              <input
                type='password'
                name='Sapo'
                id='password'
                placeholder='Password'
                onChange={handlePassword}
                autoComplete='current-password'
                value={password}
              />
            </span>
          </div>
          <button type='submit'>Sign In</button>
        </form>
        {!!error && <p>{error}</p>}
      </div>
    </div>
  );
}

export { SignIn };
