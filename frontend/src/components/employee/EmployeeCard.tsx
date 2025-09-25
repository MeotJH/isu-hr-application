import {Avatar, Button, Card, Row, Col, Typography, Space, Tag} from "antd";
const { Title, Text } = Typography;
import {CameraOutlined, SyncOutlined, UserOutlined} from "@ant-design/icons";

const EmployeeCard=() => {
    return (
        <Card style={{ marginBottom: 16, boxShadow: '0 4px 12px rgba(0, 0, 0, 0.15)', marginTop: '10px' }} bordered={false}>
            <Row gutter={36}>
                <Col style={{ position: 'relative', display: 'inline-block' }}>
                    <Avatar
                        size={100}
                        src="/path/to/image.jpg"
                        alt="프로필"
                        icon={<UserOutlined />}  // 이미지 없을 때 기본 아이콘
                    />

                    {/* 카메라 아이콘 버튼 */}
                    <Button
                        type="primary"
                        shape="circle"
                        icon={<CameraOutlined />}
                        size="small"
                        onClick={() => console.log('프로필 변경')}
                        style={{
                            position: 'absolute',
                            bottom: '5px',
                            right: '10px',
                            width: '30px',
                            height: '30px',
                            border: '2px solid white',
                            boxShadow: '0 2px 4px rgba(0,0,0,0.2)'
                        }}
                    />
                </Col>
                <Col>
                    <Space direction="vertical" size={4}>
                        <Title level={3} style={{ margin: 0, lineHeight: 1.2 }}>김직원</Title>
                        <div>
                            <Text style={{ color: '#1890ff', fontWeight: 'bold' }}>조직</Text> <Text type="secondary">A 조직</Text>
                        </div>
                        <div>
                            <Text style={{ color: '#1890ff', fontWeight: 'bold' }}>역할</Text> <Text type="secondary">개발자</Text>
                        </div>
                        <Tag icon={<SyncOutlined spin />} color="processing">
                            재직중
                        </Tag>
                    </Space>
                </Col>
            </Row>
        </Card>
    )
};


export default EmployeeCard