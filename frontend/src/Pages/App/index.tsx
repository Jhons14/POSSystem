import { useContext } from 'react'
import { useRoutes, BrowserRouter } from 'react-router-dom'
import { MainProvider, MainContext } from '../../Context'
import { Home } from '../Home'
import { ProductMenu } from '../ProductMenu'
import { SignIn } from '../SignIn'
import '@fontsource/poppins'
import './index.css'

function AppRoutes(): React.ReactElement | null {
  const { userLogged } = useContext(MainContext) as { userLogged: boolean }

  const appRoutes = useRoutes([
    {
      path: '/',
      element: userLogged ? <Home /> : <SignIn />,
      children: [{ path: ':productCategory', element: <ProductMenu /> }]
    }
  ])
  return appRoutes
}

function App(): JSX.Element {
  return (
    <MainProvider>
      <BrowserRouter>
        <AppRoutes />
      </BrowserRouter>
    </MainProvider>
  )
}
export { App }
