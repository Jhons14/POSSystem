import { useContext } from 'react';
import { Modal } from '../Modal';
import { MainContext } from '../../context';
import { OrderItems } from '../OrderItems';
import { IoMdRefresh } from 'react-icons/io';
import { CiSquareCheck } from 'react-icons/ci';
import './index.css';
export function EditOrderList({
  setOrderEditable,
  orderActive,
  handleScroll,
  orderListRef,
  order,
  setOrder,
}) {
  const { orderList, setOrderList } = useContext(MainContext);

  function onEditClientName(e) {
    e.preventDefault();
    const value = e.target.previousElementSibling.value;
    const newOrderList = orderList.map((order) => {
      if (order.id === orderActive.id) {
        return { ...order, clientName: value };
      }
      return order;
    });

    setOrderList(newOrderList);
  }

  function onConfirm(orderToInsert) {
    const newOrderList = orderList.map((order) =>
      order === orderActive ? orderToInsert : order
    );

    setOrderList(newOrderList);
    setOrderEditable(false);
  }

  return (
    <Modal stateUpdater={setOrderEditable}>
      <h1>Editar pedido</h1>
      <div>
        <label htmlFor='clientName__input'>Cliente:</label>
        <input
          name='clientName__input'
          type='text'
          placeholder={`${orderActive.clientName || 'Sin registrar'}`}
        />
        <button type='button' onClick={(e) => onEditClientName(e)}>
          Cambiar nombre
        </button>
      </div>
      <OrderItems
        handleScroll={handleScroll}
        orderActive={orderActive}
        orderListRef={orderListRef}
        editable={true}
        setOrderEditable={setOrderEditable}
        order={order}
        setOrder={setOrder}
      />
      <div className='orderList__item-footer'>
        <button
          type='button'
          className={`orderListItem__button orderListItem__button-refresh`}
          onClick={() => setOrder({ ...orderActive })}
        >
          <IoMdRefresh size={28} />
        </button>
        <button
          className={`orderListItem__button orderListItem__button-confirm`}
          type='button'
          onClick={() => onConfirm(order)}
        >
          <CiSquareCheck size={28} />
        </button>
      </div>
    </Modal>
  );
}
