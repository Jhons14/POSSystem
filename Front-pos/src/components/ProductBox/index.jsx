import { useState, useEffect } from 'react'
import { ProductDetails } from '../ProductDetails'
import { getProductById, handleProductInOrderList } from '../../utils'

import './index.css'

function ProductBox(props) {
  const [product, setProduct] = useState()

  const [productAmount, setProductAmount] = useState(0)

  //Funcion para reinciar el contador de cantidad de producto a añadir

  function onAddProductToOrderList() {
    handleProductInOrderList(
      product,
      productAmount,
      props.tableActive,
      props.orderList,
      props.setOrderList
    )
    setProductAmount(0)
  }

  useEffect(() => {
    if (props.product) {
      setProduct(props.product)
    } else {
      getProductById().then((data) => setProduct(data))
    }
  }, [])

  if (product) {
    return (
      <div className="product-box" onClick={() => onAddProductToOrderList()}>
        <ProductDetails
          product={product}
          productAmount={productAmount}
          setProductAmount={setProductAmount}
        />
      </div>
    )
  }
}
export { ProductBox }
