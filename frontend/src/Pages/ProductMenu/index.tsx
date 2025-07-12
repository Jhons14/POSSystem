import { useContext, useEffect } from 'react';
import { MainContext } from '../../context';
import { ProductList } from '../../components/ProductList';
import { ProductBox } from '../../components/ProductBox';
import { OrdersViewer } from '../../components/OrdersViewer';
import { ScreenWarning } from '../../common/ScreenWarning';
import { ScreenLoading } from '../../common/ScreenLoading';
import { ScreenError } from '../../common/ScreenError';
import { getProductsByCategory } from '../../utils';

import './index.css';

function ProductMenu(): JSX.Element {
  const {
    userLogged,
    setOrderList,
    orderList,
    error,
    setError,
    warning,
    typeProductActive,
    setTypeProductActive,
    productsByCategory,
    loading,
    setLoading,
    tableActive,
    setTableActive,
    setProductsByCategory,
  } = useContext(MainContext);

  useEffect(() => {
    setTypeProductActive(location.pathname.substring(1));
  }, [location.pathname]);

  useEffect(() => {
    if (userLogged && typeProductActive) {
      const fetchProducts = async () => {
        await getProductsByCategory(
          setLoading,
          setError,
          typeProductActive,
          setProductsByCategory
        );
      };
      fetchProducts();
    }
  }, [typeProductActive]);

  const renderView = () => {
    if (!!error && !loading) {
      return <ScreenError error={error} />;
    }
    if (!!warning && !loading) {
      return <ScreenWarning warning={warning} />;
    }

    if (loading) {
      return <ScreenLoading />;
    } else {
      return (
        <div className='product-menu-container'>
          <ProductList productsByCategory={productsByCategory}>
            {(product) => (
              <ProductBox
                key={product.productId}
                product={product}
                setOrderList={setOrderList}
                orderList={orderList}
                tableActive={tableActive}
              />
            )}
          </ProductList>
          <OrdersViewer
            tableActive={tableActive}
            orderList={orderList}
            setTableActive={setTableActive}
          />
        </div>
      );
    }
  };
  return renderView();
}

export { ProductMenu };
