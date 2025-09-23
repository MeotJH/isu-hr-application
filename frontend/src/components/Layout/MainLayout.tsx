import { Layout } from 'antd'
import { Outlet } from 'react-router-dom'
import Sidebar from './Sidebar'
import Header from './Header'

const { Content } = Layout

const MainLayout = () => {
  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Sidebar />

      <Layout style={{ marginLeft: 240 }}>
        <Header />

        <Content style={{
          padding: '24px',
          background: '#f0f2f5',
          minHeight: 'calc(100vh - 64px)'
        }}>
          <Outlet />
        </Content>
      </Layout>
    </Layout>
  )
}

export default MainLayout