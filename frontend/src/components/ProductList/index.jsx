import React from 'react'
import './index.css'

const ProductList = (props) => {
  return <div className="product-list">{props.productsByCategory.map(props.children)}</div>
}

export { ProductList }
