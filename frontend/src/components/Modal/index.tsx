import ReactDOM from 'react-dom'

import { IoCloseOutline } from 'react-icons/io5'
import './index.css'

function Modal({ children, stateUpdater }): JSX.Element {
  return ReactDOM.createPortal(
    <div className="modal__container">
      <div className="modal__object">
        <IoCloseOutline
          size={24}
          className="modal__close-icon"
          onClick={() => stateUpdater(false)}
        />
        <section className="modal__children">{children}</section>
      </div>
    </div>,
    document.getElementById('modal')
  )
}

export { Modal }
