import { useContext } from 'react';
import { MainContext } from '../../Context';
import { FaArrowLeft, FaArrowRight } from 'react-icons/fa';
import { FixedHandler } from '../FixedHandler';
import { CreateBill } from '../CreateBill';
import { OrderList } from '../OrderList';
import { Modal } from '../Modal';

import './index.css';

type Product = {
  id: string;
  name: string;
  price: number;
  quantity: number;
  totalPrice: number;
};

type Order = {
  table: string;
  clientName: string;
  products: Product[];
  totalToPay: number;
};

function OrderSection({
  tableActive,
  setTableActive,
  orderList,
}: {
  tableActive: string;
  setTableActive: (tableActive: string) => void;
  orderList: Order[];
}): JSX.Element {
  const { setOpenCreateOrder } = useContext(MainContext);

  //EFFECTS

  const orderActiveIndex = orderList?.findIndex(
    (listItem) => listItem.table === tableActive
  );
  const orderActive = orderList?.[orderActiveIndex];

  const showLeftArrow = orderActiveIndex > 0;
  const showRightArrow = orderActiveIndex < orderList?.length - 1;

  const clientName = orderActive?.clientName;

  //Calcula el total a pagar del usuario y lo agrega al estado de la orden activa

  function handleTableArrow(arrow: string) {
    if (arrow === 'left') {
      setTableActive(orderList[orderActiveIndex - 1].table);
    } else {
      setTableActive(orderList[orderActiveIndex + 1].table);
    }
  }

  const orderPrice = (): number => {
    let totalToPayValue = 0;
    orderActive.products.forEach((product) => {
      totalToPayValue += product.totalPrice;
    });

    return totalToPayValue;
  };

  if (!tableActive) {
    return (
      <Modal stateUpdater={setOpenCreateOrder}>
        <CreateBill />
      </Modal>
    );
  } else {
    return (
      <div className='order-section-container'>
        <div className='order-title_section'>
          <span>
            <FaArrowLeft
              size={24}
              className={`tables-arrow--${showLeftArrow}`}
              onClick={() => handleTableArrow('left')}
            />
          </span>
          <h1>Mesa {tableActive ? tableActive : '--'}</h1>
          <span>
            <FaArrowRight
              size={24}
              className={`tables-arrow--${showRightArrow}`}
              onClick={() => handleTableArrow('right')}
            />
          </span>
        </div>
        <span>{clientName ? clientName : 'Sin Registrar'}</span>
        <OrderList orderActive={orderActive} />
        <FixedHandler orderPrice={orderPrice()} />
      </div>
    );
  }
}
export { OrderSection };
