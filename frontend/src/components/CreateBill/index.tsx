import { useRef, useEffect, useContext, useState } from 'react';
import { addTableToOrder } from '../../utils';
import { MainContext } from '../../context';

import './index.css';
interface Order {
  orderId: string;
  orders: Array<{ id: string; name: string }>;
  table: number;
}
type MainContextType = {
  setOpenCreateOrder: (openCreateOrder: boolean) => void;
  openCreateOrder: boolean;
  setTableActive: (table: number) => void;
  orderList: Array<Order>;
  setOrderList: (orderList: Order[]) => void;
  tableActive: number;
};

export function CreateBill(): JSX.Element {
  const formRef = useRef<HTMLFormElement>(null);

  const [createOrderMessage, setCreateOrderMessage] = useState('');
  const {
    openCreateOrder,
    setOpenCreateOrder,
    setTableActive,
    orderList,
    setOrderList,
  } = useContext(MainContext) as MainContextType;

  useEffect(() => setCreateOrderMessage(''), [openCreateOrder]);

  function checkInTable(form) {
    const formData = new FormData(form);
    const tableNumber = formData.get('numero-mesa_input');
    const trimedTableNumber = tableNumber.trim(); // Obtén el valor del input y elimina espacios extra
    const parsedTableNumber = Number(trimedTableNumber); // Obtén el valor del input y elimina espacios extra

    if (parsedTableNumber >= 1) {
      const isOrderInList = orderList.some(
        (order) => order.table === parsedTableNumber
      );
      if (!isOrderInList) {
        addTableToOrder(parsedTableNumber, orderList, setOrderList);
        setTableActive(parsedTableNumber);
        setOpenCreateOrder(false);
      } else {
        setCreateOrderMessage(
          'Ya existe una orden asociada a ese numero de mesa'
        );
      }
    } else {
      setCreateOrderMessage('El valor ingresado debe ser numerico y mayor a 0');
    }
  }

  return (
    <div className='create-bill-section'>
      <h1 className='create-bill-title'>Registrar orden</h1>
      <form
        className='register-client-form'
        ref={formRef}
        onSubmit={(e) => {
          e.preventDefault();
          checkInTable(formRef.current);
        }}
      >
        <div>
          <label htmlFor='numero-mesa_input' className='numero-mesa_label'>
            Número de mesa:
          </label>
          <input
            inputMode='numeric'
            name='numero-mesa_input'
            id='numero-mesa_input'
            className='numero-mesa_input'
            placeholder='Ingrese el número'
            autoComplete='off'
            required
            min='1'
            pattern='[0-9]+'
            title='Por favor ingrese un número válido'
          />
        </div>
        <button type='submit'>Registrar</button>
      </form>
      <p className='create-order-message'>{createOrderMessage}</p>
    </div>
  );
}
