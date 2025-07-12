import { useEffect, useState } from 'react'
import { MenuButton } from '../MenuButton'
import { getAllCategories } from '../../utils'
import './Menus.css'

export function Menus(props) {
  const [categories, setCategories] = useState([])

  useEffect(() => {
    const fetchCategories = async () => {
      props.setLoading(true)
      try {
        const response = await getAllCategories()
        setCategories(response)
      } catch (error) {
        setError(error)
      }
      props.setLoading(false)
    }
    fetchCategories()
  }, [])

  return (
    <div className="Menus">
      {!!categories.length > 0 &&
        categories?.map((category) => {
          const categoryArray = [category]
          return (
            <MenuButton
              key={category.categoryId}
              category={categoryArray}
              setLoading={props.setLoading}
              setWarning={props.setWarning}
            />
          )
        })}
    </div>
  )
}
