import { FaSearch } from 'react-icons/fa'

import { useContext, useEffect, useState } from 'react'
import { getAllProducts, signOut } from '../../utils'
import { MainContext } from '../../Context'
import './NavBar.css'

interface Product {
  name: string
  // other properties of Product
}

function NavBar() {
  const [searchField, setSearchField] = useState('')
  const [products, setProducts] = useState([])

  const { setProductsByCategory, setWarning, warning } = useContext(MainContext) as {
    setProductsByCategory: (products: Array<Product>) => void
    setWarning: (warning: string) => void
    warning: string
  }

  useEffect(() => {
    if (searchField.length >= 0) {
      const fetch = async () => {
        await getAllProducts().then((data) => setProducts(data))
      }
      fetch()
    }
  }, [])

  function useSearch(searchField): void {
    warning && setWarning(false)

    const newFilteredProducts = products.filter((product: Product) =>
      product.name.toLowerCase().includes(searchField.toLowerCase())
    )

    newFilteredProducts.length === 0
      ? setWarning('No se encontraron productos en la busqueda')
      : setProductsByCategory(newFilteredProducts)
    setSearchField('')
  }

  const onSearchingChange = (textToSearch) => setSearchField(textToSearch)

  return (
    <nav className="NavBar">
      <span>KHD</span>
      <div>
        <section id="Search-section">
          <input
            autoComplete="off"
            id="Search-input"
            onChange={(e) => onSearchingChange(e.target.value)}
            value={searchField}
            type="text"
            placeholder="Buscar producto"
          />
          <button
            title="Search-button"
            id="Search-button"
            type="button"
            onClick={() => useSearch(searchField)}
          >
            <FaSearch id="Search-icon" />
          </button>
        </section>
      </div>
      <div>Ir al perfil</div>
      <div className="signOut" onClick={() => signOut()}>
        <span>Cerrar sesión</span>
      </div>
    </nav>
  )
}
export { NavBar }
