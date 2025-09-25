import { Layout } from 'antd'
import { Outlet } from 'react-router-dom'
import Sidebar from './Sidebar'
import Header from './Header'

const { Content } = Layout

const MainLayout = () => {
  return (
    <Layout style={{ height: '100vh', display: 'flex', flexDirection: 'column', overflow: 'hidden' }}>
        <Header />
        <div style={{ minHeight: '0', display: 'flex',  flex: '1 1 auto', overflow: 'hidden'}}>
            <Sidebar />
            <Content style={{
                padding: '12px',
                background: '#f0f2f5',
                minWidth: 0,
                minHeight: 0,
                overflow: 'hidden',
                flex: '1 1 auto',
                display: 'flex',
            }}>
                <Outlet />
            </Content>
        </div>
    </Layout>
  )
}

export default MainLayout