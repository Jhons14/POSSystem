import { createContext, useEffect, useReducer, useState } from 'react';

export const authContext = createContext();

export function AuthProvider({ children }): JSX.Element {
  const [auth, setAuth] = useState({});

  return (
    <authContext.Provider value={{ auth, setAuth }}>
      {children}
    </authContext.Provider>
  );
}

export const MainContext = createContext();

export function MainProvider({ children }) {
  const [state, dispatch] = useReducer(reducer, initialState());

  //STATE UPDATERS
  useEffect(() => {
    sessionStorage.getItem('token') && onSetUserLogged(true);
  }, []);
  ////USER LOGIN
  const onSetUserLogged = (userLogged) =>
    dispatch({
      type: actionTypes.setUserLogged,
      payload: userLogged,
    });

  ////ProductsByCategory
  const onSetProductsByCategory = (productsByCategory) =>
    dispatch({
      type: actionTypes.setProductsByCategory,
      payload: productsByCategory,
    });

  const onSetLoading = (isActive) =>
    dispatch({ type: actionTypes.setLoading, payload: isActive });

  const onSetError = (error) =>
    dispatch({ type: actionTypes.setError, payload: error });

  const onSetWarning = (warning) =>
    dispatch({ type: actionTypes.setWarning, payload: warning });

  const onSetTypeProductActive = (typeProductActive) =>
    dispatch({
      type: actionTypes.setTypeProductActive,
      payload: typeProductActive,
    });

  const onSetOrderList = (productsByCategory) =>
    dispatch({ type: actionTypes.setOrderList, payload: productsByCategory });

  const onSetTableActive = (tableActive) =>
    dispatch({ type: actionTypes.setTableActive, payload: tableActive });

  const onsetOpenModal = (openCreateOrder) =>
    dispatch({
      type: actionTypes.setOpenCreateOrder,
      payload: openCreateOrder,
    });

  return (
    <MainContext.Provider
      value={{
        userLogged: state.userLogged,
        error: state.error,
        warning: state.warning,
        loading: state.loading,
        productsByCategory: state.productsByCategory,
        typeProductActive: state.typeProductActive,
        orderList: state.orderList,
        isModalOpen: state.isModalOpen,
        openCreateOrder: state.openCreateOrder,
        tableActive: state.tableActive,
        setUserLogged: onSetUserLogged,
        setError: onSetError,
        setWarning: onSetWarning,
        setLoading: onSetLoading,
        setProductsByCategory: onSetProductsByCategory,
        setTypeProductActive: onSetTypeProductActive,
        setOrderList: onSetOrderList,
        setTableActive: onSetTableActive,
        setOpenCreateOrder: onsetOpenModal,
      }}
    >
      {children}
    </MainContext.Provider>
  );
}

const initialState = () => {
  return {
    openCreateOrder: true,
    userLogged: false,
    isModalOpen: false,
    error: false,
    loading: false,
    productsByCategory: [],
    typeProductActive: '',
    orderList: [],
  };
};
const reducerObject = (state, payload) => ({
  [actionTypes.setUserLogged]: {
    ...state,
    userLogged: payload,
  },
  [actionTypes.setError]: {
    ...state,
    error: payload,
  },
  [actionTypes.setWarning]: {
    ...state,
    warning: payload,
  },
  [actionTypes.setLoading]: {
    ...state,
    loading: payload,
  },
  [actionTypes.setTypeProductActive]: {
    ...state,
    typeProductActive: payload,
  },
  [actionTypes.setProductsByCategory]: {
    ...state,
    productsByCategory: payload,
  },
  [actionTypes.setOrderList]: {
    ...state,
    orderList: payload,
  },
  [actionTypes.setTableActive]: {
    ...state,
    tableActive: payload,
  },
  [actionTypes.setOpenCreateOrder]: {
    ...state,
    openCreateOrder: payload,
  },
});

const actionTypes = {
  setUserLogged: 'USER_LOGGED',
  setError: 'ERROR',
  setWarning: 'WARNING',
  setLoading: 'LOADING',
  setTypeProductActive: 'TYPE_PRODUCT_ACTIVE',
  setProductsByCategory: 'PRODUCTS_BY_CATEGORY',
  setOrderList: 'ORDER_LIST',
  setTableActive: 'TABLE_ACTIVE',
  setOpenCreateOrder: 'OPEN_MODAL',
};

const reducer = (state, action) => {
  return reducerObject(state, action.payload)[action.type] || state;
};
