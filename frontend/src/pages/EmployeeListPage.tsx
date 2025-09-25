import { Row, Col, Card, Typography,  Flex, Statistic} from "antd";
import {SettingOutlined, UserOutlined} from "@ant-design/icons";
import OrganizationTree from "../components/EmployeeList/OrganizationTree.tsx";

const {Title} = Typography;

const EmployeeListPage = () => {

    return (
        <div style={{flex: '1 1 auto', display: 'flex' , flexDirection: 'column', minHeight: 0, overflow: 'hidden' }}>
            {/* 헤더 */}
            <div style={{ flex: '0 0 auto' }}>
                <Card
                    style={{
                        borderRadius: 8
                    }}

                >
                    <Title level={4} style={{ margin: 0 }}>
                        직원 관리
                    </Title>
                </Card>
            </div>

            {/* 메인 콘텐츠 - 고정 높이로 설정 */}
            <div style={{ flex: 1, minHeight: 0, marginTop: '8px' }}>
                <Row gutter={8} style={{ height: '100%' }}>
                    {/* 왼쪽 조직도 카드 */}
                    <Col span={8} style={{ height: '100%' }}>
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
                    <Col span={16} style={{ height: '100%' }}>
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
                                    overflow: 'auto',
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
                            {/* 스크롤 테스트를 위한 더미 데이터 */}
                            {Array.from({length: 50}, (_, index) => (
                                <div key={index} style={{
                                    padding: '12px',
                                    border: '1px solid #f0f0f0',
                                    borderRadius: '6px',
                                    marginBottom: '8px',
                                    backgroundColor: '#fafafa'
                                }}>
                                    <strong>직원 {index + 1}</strong>
                                    <div>부서: {index % 2 === 0 ? '개발팀' : '마케팅팀'}</div>
                                    <div>직급: {index % 3 === 0 ? '팀장' : index % 3 === 1 ? '선임' : '사원'}</div>
                                    <div>이메일: employee{index + 1}@company.com</div>
                                </div>
                            ))}
                        </Card>
                    </Col>
                </Row>
            </div>
        </div>
    );
}

export default EmployeeListPage