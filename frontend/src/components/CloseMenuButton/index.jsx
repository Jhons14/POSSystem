import { useNavigate } from 'react-router-dom';
import './CloseMenuButton.css';

const navigate = useNavigate();
function CloseMenuButton(props) {
  return (
    <button className='CloseMenuButton' onClick={() => navigate(-1)}>
      X
    </button>
  );
}

export { CloseMenuButton };
