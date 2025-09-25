import { Layout } from 'antd'
import { Outlet } from 'react-router-dom'
import Sidebar from './Sidebar'
import Header from './Header'

const { Content } = Layout

const MainLayout = () => {
  return (
    <Layout style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column' }}>
        <Header />
        <div style={{ minHeight: '0', display: 'flex',  flex: '1 1 auto'}}>
            <Sidebar />
            <Content style={{
                padding: '12px',
                background: '#f0f2f5',
                minHeight: '0',
                overflow: 'hidden'
            }}>
                <Outlet />
            </Content>
        </div>
    </Layout>
  )
}

export default MainLayout