import { Menu, Layout } from 'antd'
import { useNavigate, useLocation } from 'react-router-dom'
import {
  DashboardOutlined,
  TeamOutlined,
  UserOutlined,
  CalendarOutlined,
  FileTextOutlined,
  SettingOutlined,
  IdcardOutlined,
  SwapOutlined,
  UserAddOutlined
} from '@ant-design/icons'

const { Sider } = Layout

const Sidebar = () => {
  const navigate = useNavigate()
  const location = useLocation()

  const menuItems = [
    {
      key: '/dashboard',
      icon: <DashboardOutlined />,
      label: '대시보드'
    },
    {
      key: 'employees',
      icon: <TeamOutlined />,
      label: '직원 관리',
      children: [
        {
          key: '/employees',
          icon: <IdcardOutlined />,
          label: '직원 관리'
        },
        {
          key: '/employees/transfer',
          icon: <SwapOutlined />,
          label: '인사발령'
        },
        {
          key: '/employees/new',
          icon: <UserAddOutlined />,
          label: '신규직원'
        }
      ]
    },
    {
      key: '/organization',
      icon: <UserOutlined />,
      label: '조직도'
    },
    {
      key: '/attendance',
      icon: <CalendarOutlined />,
      label: '근태 관리'
    },
    {
      key: '/payroll',
      icon: <FileTextOutlined />,
      label: '급여 관리'
    },
    {
      key: '/settings',
      icon: <SettingOutlined />,
      label: '설정'
    }
  ]

  const handleMenuClick = ({ key }: { key: string }) => {
    if (key.startsWith('/')) {
      navigate(key)
    }
  }

  return (
    <Sider
      theme="light"
      width={240}
      style={{
        overflow: 'auto',
        height: '100vh',
        position: 'fixed',
        left: 0,
        top: 0,
        bottom: 0,
        borderRight: '1px solid #f0f0f0'
      }}
    >
      {/* 로고 영역 */}
      <div style={{
        height: '64px',
        padding: '16px',
        borderBottom: '1px solid #f0f0f0',
        display: 'flex',
        alignItems: 'center',
        gap: '12px'
      }}>
        <TeamOutlined style={{ fontSize: '24px', color: '#1890ff' }} />
        <span style={{ fontWeight: 'bold', fontSize: '16px' }}>인사 관리</span>
      </div>

      {/* 메뉴 */}
      <Menu
        mode="inline"
        selectedKeys={[location.pathname]}
        items={menuItems}
        onClick={handleMenuClick}
        style={{
          borderRight: 0,
          height: 'calc(100vh - 64px)'
        }}
      />
    </Sider>
  )
}

export default Sidebar