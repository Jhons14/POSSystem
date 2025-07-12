const SERVER_URL = import.meta.env.VITE_SERVER_URL
const AUTHENTICATION_URL = `${SERVER_URL}/auth/authenticate`

//PRODUCTS URL
const GET_ALL_PRODUCTS_URL = `${SERVER_URL}/products/all`
const UPLOAD_PRODUCTIMG_URL = `${SERVER_URL}/files/upload/image/product/`
const UPDATE_PRODUCT_URL = `${SERVER_URL}/products/update/`

//CATEGORIES URL
const GET_ALL_CATEGORIES_URL = `${SERVER_URL}/category/all`

//AUTHENTICATION
async function authenticate(username, password, setUserLogged, setError) {
  const credentials = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      username: username,
      password: password
    })
  }

  const response = await fetch(AUTHENTICATION_URL, credentials).catch(() =>
    setError('Error desconocido, por favor intente mas tarde')
  )
  if (response) {
    if (!response.ok) {
      const jsonRes = await response.json()
      setError(jsonRes.message)
    } else {
      const jsonRes = await response.json()
      sessionStorage.setItem('token', jsonRes.jwt)
      setUserLogged(true)
    }
  }
}

//SIGNOUT
function signOut() {
  sessionStorage.removeItem('token')
  window.location.reload()
}

//GET ALL PRODUCTS
async function getAllProducts() {
  const parsedToken = sessionStorage.getItem('token')

  return await fetch(GET_ALL_PRODUCTS_URL, {
    method: 'GET',
    headers: {
      Authorization: `Bearer ${parsedToken}`
    }
  }).then((data) => (data = data.json()))
}

//GET PRODUCTS BY CATEGORY
async function getProductsByCategory(setLoading, setError, categoryId, setProductsByCategory) {
  const GET_PRODUCTS_BY_CATEGORY_URL = `${SERVER_URL}/products/category/${categoryId}`

  const parsedToken = sessionStorage.getItem('token')
  setLoading(true)

  const response = await fetch(GET_PRODUCTS_BY_CATEGORY_URL, {
    method: 'GET',
    headers: {
      Authorization: `Bearer ${parsedToken}`
    }
  }).catch(() => setError('Error desconocido. Por favor intente mas tarde'))

  if (!!response) {
    if (!!response.ok) {
      const jsonRes = await response.json()
      setProductsByCategory(jsonRes)
    } else if (response.status === 401) {
      const jsonRes = await response.json()
      setError(jsonRes.errorMessage)
    }
  }
  setLoading(false)
}

//GET PRODUCT BY ID
async function getProductById() {
  const currentURL = window.location.pathname
  const productIdInURL = currentURL.match(/[^/]+$/)[0]
  const GET_PRODUCT_BY_ID_URL = `${SERVER_URL}/products/${productIdInURL}`
  const parsedToken = sessionStorage.getItem('token')
  const product = await fetch(GET_PRODUCT_BY_ID_URL, {
    method: 'GET',
    headers: {
      Authorization: `Bearer ${parsedToken}`
    }
  })
    .then((data) => (data = data.json()))
    .catch((error) => {
      window.setError(error)
    })
  return product
}

//UPLOAD (AND UPDATE A PRODUCT IMG)
async function uploadImg(product) {
  const parsedToken = sessionStorage.getItem('token')
  var formData = new FormData()
  var fileInput = document.getElementById(`fileInput`)

  formData.append('file', fileInput.files[0])

  fetch(`${UPLOAD_PRODUCTIMG_URL + product.productId}`, {
    method: 'POST',
    body: formData,
    headers: {
      Authorization: `Bearer ${parsedToken}`
    }
  }).then((response) => response.text())

  const productBody = { img_url: fileInput.files[0].name }

  fetch(`${UPDATE_PRODUCT_URL + product.productId}`, {
    method: 'POST',
    body: JSON.stringify(productBody),
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${parsedToken}`
    }
  })
}

//GET ALL CATEGORIES
async function getAllCategories() {
  const parsedToken = sessionStorage.getItem('token')
  const categories = await fetch(GET_ALL_CATEGORIES_URL, {
    method: 'GET',
    headers: {
      Authorization: `Bearer ${parsedToken}`
    }
  })
    .then((data) => (data = data.json()))
    .catch((error) => {
      setError(error)
    })
  return categories
}

//UPDATE A CATEGORY
async function updateCategory(categoryId) {
  const UPDATE_CATEGORY_URL = `${SERVER_URL}/category/update/${categoryId}`
  const UPLOAD_CATEGORY_IMG_URL = `${SERVER_URL}/category/update/${categoryId}`
  const parsedToken = sessionStorage.getItem('token')
  var formData = new FormData()
  var fileInput = document.getElementById(`fileInput${categoryId}`)

  formData.append('file', fileInput.files[0])
  fetch(UPLOAD_CATEGORY_IMG_URL, {
    method: 'POST',
    body: formData,
    headers: {
      Authorization: `Bearer ${parsedToken}`
    }
  })
    .then((response) => response.text())
    .catch((error) => {
      console.error('Error:', error)
    })
  const categoryBody = { img: fileInput.files[0].name }

  fetch(UPDATE_CATEGORY_URL, {
    method: 'POST',
    body: JSON.stringify(categoryBody),
    headers: {
      Authorization: `Bearer ${parsedToken}`,
      'Content-Type': 'application/json'
    }
  }).finally(window.location.reload())
}

//ADD PRODUCT
function handleProductInOrderList(product, productAmount, tableActive, orderList, setOrderList) {
  function searchIdProduct(idToSearch, orderToModify) {
    for (let i = 0; i < orderToModify.products.length; i++) {
      if (orderToModify.products[i].id === idToSearch) {
        return i
      }
    }
    return -1
  }

  //Busca index de lista en la mesa activa, si no existe retorna -1
  const indexInOrderToModify = orderList.findIndex((order) => order.table === tableActive)
  //Si ya existe la orden en la mesa activa
  if (indexInOrderToModify !== -1) {
    //Obtener el index del producto que se agregara en base al id
    const productIndex = searchIdProduct(product.productId, orderList[indexInOrderToModify])
    //Si el producto ya existe en la lista activa, s
    if (productIndex != -1) {
      //Crea una copia del arreglo y hace las modificaciones pertinentes en el estado para evitar modificar el estado de manera directa

      const orderItem = orderList[indexInOrderToModify]
      const product = orderItem.products[productIndex]
      const newProductsArray = [...orderItem.products]
      newProductsArray[productIndex] = {
        ...product,
        quantity: product.quantity + productAmount,
        totalPrice: (product.quantity + productAmount) * product.price
      }
      const newOrderListArray = [
        ...orderList.slice(0, indexInOrderToModify),
        { ...orderItem, products: newProductsArray },
        ...orderList.slice(indexInOrderToModify + 1)
      ]

      //Actualiza estado
      setOrderList(newOrderListArray)
      //Reinicia contador de catidad a agregar
    } else {
      //Si el producto no existe en la lista activa, se hace necesario crearlo
      //Si la cantidad a agregar es diferente a cero, de lo contrario no suma nada ya que la seleccion es de cero
      if (productAmount !== 0) {
        const orderItem = orderList[indexInOrderToModify]
        const newProductsArray = [
          ...orderItem.products,
          {
            id: product.productId,
            name: product.name,
            quantity: productAmount,
            price: product.price,
            totalPrice: productAmount * product.price
          }
        ]
        const newOrderListArray = [
          ...orderList.slice(0, indexInOrderToModify),
          { ...orderItem, products: newProductsArray },
          ...orderList.slice(indexInOrderToModify + 1)
        ]
        //Actualiza estado
        setOrderList(newOrderListArray)
        //Reinicia contador de catidad a agregar
      }
    }
  }

  // setIDIdentator(IDIdentator + 1);
}

//DELETE PRODUCT
function deleteProductFromOrderList(productIndex, orderToModifyIndex, orderList) {
  let newOrderList = [...orderList]
  newOrderList[orderToModifyIndex].products.splice(productIndex, 1)
  return newOrderList
}

function addTableToOrder(tableNumber, orderList, setOrderList) {
  const indexToPushOrder = orderList.findIndex((order) => order.table > tableNumber)

  if (indexToPushOrder === -1) {
    const newOrderListArray = [
      ...orderList,
      { key: orderList.length, orderId: orderList.length, table: tableNumber, products: [] }
    ]
    setOrderList(newOrderListArray)
  } else {
    const newOrderListArray = [
      ...orderList.slice(0, indexToPushOrder),
      { key: orderList.length, orderId: orderList.length, table: tableNumber, products: [] },
      ...orderList.slice(indexToPushOrder)
    ]
    setOrderList(newOrderListArray)
  }
}

//REPLACE PRODUCT IN ORDER
function replaceProductInOrderList(
  productToReplace,
  orderToModifyIndex,
  orderList,
  setOrderList,
  productQuantity
) {
  const productToReplaceIndex = orderList[orderToModifyIndex].products.findIndex(
    (product) => product === productToReplace
  )
  const orderItem = orderList[orderToModifyIndex]
  const product = orderItem.products[productToReplaceIndex]
  let newOrderListArray = [...orderList]
  let newProductsArray = [...orderItem.products]
  newProductsArray[productToReplaceIndex] = {
    ...product,
    quantity: productQuantity
  }
  newOrderListArray[orderToModifyIndex].products = newProductsArray
  setOrderList(newOrderListArray)
}

//ModifyProduct  IN ORDERLIST
export {
  handleProductInOrderList,
  deleteProductFromOrderList,
  replaceProductInOrderList,
  addTableToOrder,
  getAllProducts,
  authenticate,
  getProductsByCategory,
  getProductById,
  uploadImg,
  signOut,
  updateCategory,
  getAllCategories
}
