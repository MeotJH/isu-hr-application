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
              flex: '0 0 240px',
              borderRight: '1px solid #f0f0f0',
              display: 'flex',
              flexDirection: 'column', // ← 핵심
              minHeight: 0            // ← 수축 허용
          }}
      >
          {/* 로고 영역 (고정 높이) */}
          <div style={{
              flex: '0 0 64px',
              padding: '16px',
              borderBottom: '1px solid #f0f0f0',
              display: 'flex',
              alignItems: 'center',
              gap: 12
          }}>
              <TeamOutlined style={{ fontSize: 24, color: '#1890ff' }} />
              <span style={{ fontWeight: 'bold', fontSize: 16 }}>인사 관리</span>
          </div>

          {/* 메뉴 컨테이너 (남은 영역 전부 차지 + 스크롤) */}
          <div style={{ flex: '1 1 auto', minHeight: 0, overflow: 'auto' }}>
              <Menu
                  mode="inline"
                  selectedKeys={[location.pathname]}
                  items={menuItems}
                  onClick={handleMenuClick}
                  style={{ borderRight: 0 }}
              />
          </div>
      </Sider>

  )
}

export default Sidebar