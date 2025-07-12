import { FaMinusSquare, FaPlusSquare } from 'react-icons/fa'

import './index.css'

function ProductDetails({ productAmount, setProductAmount, product }): JSX.Element {
  function handleOption(event, payload): void {
    event.stopPropagation()
    // Crea una copia del objeto con la actualización
    const newProductAmount = payload === 'plus' ? productAmount + 1 : Math.max(0, productAmount - 1) // evita valores negativos

    setProductAmount(newProductAmount)
  }

  function productQuantityHandler(): JSX.Element {
    return (
      <div className="options-container">
        <div className="buttons-container quantity-buttons-container">
          <FaMinusSquare
            size={24}
            onClick={(e) => handleOption(e, 'subtrack')}
            className="quantity-button quantity-button-minus"
          />

          <span id="quantity-counter">{productAmount}</span>
          <FaPlusSquare
            size={24}
            onClick={(e) => handleOption(e, 'plus')}
            className="quantity-button quantity-button-plus"
          />
        </div>
        <span>${product.price}</span>
      </div>
    )
  }

  const renderOptionList = () => (
    <div id="product-details-container">
      <span className="product-title">{product.name}</span>
      {productQuantityHandler()}
    </div>
  )

  return renderOptionList()
}
export { ProductDetails }
