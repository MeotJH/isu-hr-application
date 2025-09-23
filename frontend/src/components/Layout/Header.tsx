import { Button, Avatar, Space, Typography, Layout } from 'antd'
import { UserOutlined, LogoutOutlined, SettingOutlined } from '@ant-design/icons'
import { useNavigate } from 'react-router-dom'
import { useAuthStore } from '../../stores/authStore'

const { Header: AntHeader } = Layout
const { Text } = Typography

const Header = () => {
  const { user, logout } = useAuthStore()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <AntHeader style={{
      background: '#fff',
      padding: '0 24px',
      borderBottom: '1px solid #f0f0f0',
      display: 'flex',
      justifyContent: 'flex-end',
      alignItems: 'center'
    }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
        <Space>
          <Avatar icon={<UserOutlined />} />
          <div>
            <Text strong>{user?.name || user?.username}</Text>
            <br />
            <Text type="secondary" style={{ fontSize: '12px' }}>
              {user?.department} · {user?.position}
            </Text>
          </div>
        </Space>

        <Button
          icon={<SettingOutlined />}
          type="text"
          onClick={() => navigate('/settings')}
        />

        <Button
          icon={<LogoutOutlined />}
          type="text"
          onClick={handleLogout}
        >
          로그아웃
        </Button>
      </div>
    </AntHeader>
  )
}

export default Header