import React from 'react';
import { Outlet } from 'react-router-dom';
import Layout from '../../components/Layout';
import { MainContext } from '../../context';
import { Menus } from '../../components/Menus';
import { OrderSection } from '../../components/OrderSection';
import { NavBar } from '../../components/NavBar';
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

function Home(): JSX.Element {
  const {
    setError,
    setLoading,
    tableActive,
    setTableActive,
    orderList,
    setWarning,
  } = React.useContext(MainContext) as {
    error: boolean;
    setError: (error: string) => void;
    setWarning: (warning: string) => void;
    setLoading: (isLoading: boolean) => void;
    setTableActive: (table: string) => void;
    tableActive: string;
    orderList: Array<Order>;
  };

  return (
    <div>
      <NavBar />
      <Layout>
        <div className='home-container'>
          <Menus
            setError={setError}
            setLoading={setLoading}
            setWarning={setWarning}
          />
          <Outlet />
          <OrderSection
            tableActive={tableActive}
            setTableActive={setTableActive}
            orderList={orderList}
          />
        </div>
      </Layout>
    </div>
  );
}
export { Home };
