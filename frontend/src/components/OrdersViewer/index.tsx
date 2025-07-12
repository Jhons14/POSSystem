import { useContext, useEffect } from 'react'
import { FaPlus } from 'react-icons/fa'
import { MainContext } from '../../Context'
import { CreateBill } from '../CreateBill'
import { Modal } from '../Modal'

import './index.css'

interface MainContextType {
  openCreateOrder: boolean
  setOpenCreateOrder: (openCreateOrder: boolean) => void
}

interface Order {
  orderId: string
  orders: Array<{ id: string; name: string }>
  table: number
}

export function OrdersViewer({
  orderList,
  setTableActive,
  tableActive
}: {
  orderList: Array<Order>
  tableActive: number
  setTableActive: (table: number) => void
}): JSX.Element {
  const { openCreateOrder, setOpenCreateOrder } = useContext(MainContext) as MainContextType

  useEffect(() => {
    if (tableActive) {
      setOpenCreateOrder(false)
    }
  }, [tableActive])

  return (
    <div className="orders-viewer">
      {!!openCreateOrder && !!tableActive && (
        <Modal stateUpdater={setOpenCreateOrder}>
          <CreateBill />
        </Modal>
      )}
      <span>Mesas</span>
      <div className="orders">
        {orderList?.map((order) => (
          <button
            key={order.orderId}
            type="button"
            onClick={() => setTableActive(order.table)}
            className="table-button"
          >
            {order.table}
          </button>
        ))}
        <button
          type="button"
          className="table-button create-table-button"
          onClick={() => {
            setOpenCreateOrder(true)
          }}
        >
          <FaPlus />
        </button>
      </div>
    </div>
  )
}
