import { useContext } from 'react'

import { FaMinusSquare, FaPlusSquare } from 'react-icons/fa'

import { RiDeleteBin6Line } from 'react-icons/ri'

import './index.css'

export function OrderItems({ handleScroll, orderListRef, editable, order, setOrder }) {
  function handleQuantityChange(action, productIndex) {
    const newOrderActiveProducts = order.products.map((product) => ({ ...product }))
    if (action === 'plus') {
      newOrderActiveProducts[productIndex].quantity++
    } else if (action === 'minus') {
      if (newOrderActiveProducts[productIndex].quantity > 1) {
        newOrderActiveProducts[productIndex].quantity--
      }
    }
    const newOrder = { ...order, products: newOrderActiveProducts }
    setOrder(newOrder)
  }

  function handleDeleteProduct(productIndex) {
    const modifiedProductList = order.products.filter((product, index) => index !== productIndex)
    const newOrder = { ...order, products: modifiedProductList }
    console.log(newOrder)

    setOrder(newOrder)
  }

  return (
    <div className="order-list" id="order-list" onScroll={() => handleScroll()} ref={orderListRef}>
      {order.products?.map((product, productIndex) => (
        <div key={product.id} className="order-list__item">
          <div className={`orderList__item-delete orderList__item-delete--${editable}`}>
            <RiDeleteBin6Line
              className={` orderList__item-delete-icon--${editable}`}
              onClick={() => {
                handleDeleteProduct(productIndex)
              }}
            />
          </div>

          <span>{product?.name}</span>
          <div className="quantity-handler">
            <div>
              <FaMinusSquare
                className={`productOrderQuantity-button productOrderQuantity-button--${editable}`}
                size={18}
                onClick={() => handleQuantityChange('minus', productIndex)}
              />
            </div>
            <span>{product?.quantity}</span>
            <div>
              <FaPlusSquare
                className={`productOrderQuantity-button productOrderQuantity-button--${editable}`}
                size={18}
                onClick={() => handleQuantityChange('plus', productIndex)}
              />
            </div>
          </div>
          <span className="order-list__item-total-price">${product?.totalPrice}</span>
        </div>
      ))}
    </div>
  )
}
