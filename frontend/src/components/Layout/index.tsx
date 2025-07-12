import './index.css'

function Layout({ children }: { children: React.JSX.Element }): React.JSX.Element {
  return <div className="layout">{children}</div>
}

export default Layout
