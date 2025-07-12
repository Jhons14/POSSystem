import { useNavigate } from 'react-router-dom'
import './index.css'

function MenuButton(props) {
  const navigate = useNavigate()

  const category = props.category[0]

  return (
    <div
      style={{
        display: 'flex',
        alignItems: 'center',
        flexDirection: 'column'
      }}
    >
      <div
        className={`MenuButton ${category.category}Button`}
        onClick={() => {
          props.setWarning('')
          navigate(category.categoryId.toString())
        }}
      >
        <span>{category.category}</span>
      </div>
    </div>
  )
}

export { MenuButton }
