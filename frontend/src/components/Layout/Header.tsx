import { Button, Avatar, Space, Typography, Layout } from 'antd'
import { UserOutlined, LogoutOutlined } from '@ant-design/icons'
import { useNavigate } from 'react-router-dom'
import { useAuthStore } from '../../stores/authStore'
import styles from './Header.module.scss'

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
    <AntHeader className={styles.header}>
      <div className={styles.userSection}>
        <Space className={styles.userInfo}>
          <Avatar icon={<UserOutlined />} />
          <Space direction="horizontal" size={12}>
            <Text strong className={styles.userName}>
              {user?.name || user?.username}
            </Text>
            <Text type="secondary" className={styles.userDetails}>
              {user?.department} · {user?.position}
            </Text>
          </Space>
        </Space>
        <Button
          icon={<LogoutOutlined />}
          type="text"
          onClick={handleLogout}
          className={styles.logoutButton}
        >
          로그아웃
        </Button>
      </div>
    </AntHeader>
  )
}

export default Header