import {Row, Col, Card, Typography, Flex, Statistic, Button} from "antd";
import {SettingOutlined, UserOutlined} from "@ant-design/icons";
import OrganizationTree from "../components/EmployeeList/OrganizationTree.tsx";
import EmployeeInfiniteTable from "../components/EmployeeList/EmployeeInfiniteTable.tsx";

const {Title} = Typography;



const EmployeeListPage = () => {

    return (
        <div style={{flex: '1 1 auto', display: 'flex' , flexDirection: 'column', minHeight: 0, overflow: 'hidden' }}>
            {/* 헤더 */}
            <div style={{ flex: '0 0 auto',boxShadow: '0 4px 12px rgba(0, 0, 0, 0.15)', }}>
                <Card
                    style={{
                        borderRadius: 8
                    }}

                >
                    <Row justify="space-between">
                        <Col span={4} >
                            <Title level={4} style={{ margin: 0 }}>
                                직원 관리
                            </Title>
                        </Col>
                        <Col span={2} >
                            <Button type="primary">
                                직원 추가
                            </Button>
                        </Col>
                    </Row>

                </Card>
            </div>

            {/* 메인 콘텐츠 - 고정 높이로 설정 */}
            <div style={{ flex: 1, minHeight: 0, marginTop: '8px' }}>
                <Row gutter={8} style={{ height: '100%' }}>
                    {/* 왼쪽 조직도 카드 */}
                    <Col span={8} style={{ height: '100%' ,boxShadow: '0 4px 12px rgba(0, 0, 0, 0.15)'}}>
                        <Card
                            title={
                                <Flex justify="space-between" align="center">
                                    <span>이수시스템</span>
                                    <SettingOutlined style={{ color: '#8c8c8c' }} />
                                </Flex>
                            }
                            style={{
                                height: '100%',
                                display: 'flex',
                                flexDirection: 'column'
                            }}
                            styles={{
                                body: {
                                    flex: '1 1 auto',
                                    minHeight: 0,
                                    overflow: 'auto',
                                    padding: '16px'
                                }
                            }}
                        >
                            <OrganizationTree />
                        </Card>
                    </Col>

                    {/* 오른쪽 직원 정보 카드 */}
                    <Col span={16} style={{ height: '100%',boxShadow: '0 4px 12px rgba(0, 0, 0, 0.15)', }}>
                        <Card
                            title="직원 정보"
                            style={{
                                height: '100%',
                                display: 'flex',
                                flexDirection: 'column'
                            }}
                            styles={{
                                body: {
                                    flex: '1 1 auto',
                                    minHeight: 0,
                                    overflow: 'hidden',
                                    padding: '16px'
                                }
                            }}
                        >
                            <Statistic
                                title="총 직원 수"
                                value={30}
                                suffix="명"
                                prefix={<UserOutlined />}
                                valueStyle={{ color: '#1890ff' }}
                                style={{ marginBottom: 16 }}
                            />
                            <EmployeeInfiniteTable
                                pageSize={200}
                            />
                        </Card>
                    </Col>
                </Row>
            </div>
        </div>
    );
}

export default EmployeeListPage