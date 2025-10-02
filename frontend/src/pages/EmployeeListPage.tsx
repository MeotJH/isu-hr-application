import {Row, Col, Card, Typography, Flex, Statistic, Button, Drawer, Form, Input, Space, DatePicker, App} from "antd";
import {SettingOutlined, UserOutlined} from "@ant-design/icons";
import OrganizationTree from "../components/EmployeeList/OrganizationTree.tsx";
import EmployeeInfiniteTable from "../components/EmployeeList/EmployeeInfiniteTable.tsx";
import {useState} from "react";
import {useMutation} from "@tanstack/react-query";
import { Dayjs } from 'dayjs';
import { apiFetch } from '../utils/api';

const {Title} = Typography;

interface EmployeeFormValues {
    sabun: string;
    name: string;
    empYmd: Dayjs;
    birYmd: Dayjs;
    email: string;
    address: string;
}

// Dayjs를 string으로 변환하는 타입
type EmployeeApiPayload = Omit<EmployeeFormValues, 'empYmd' | 'birYmd'> & {
    empYmd: string;
    birYmd: string;
};

const EmployeeListPage = () => {
    const [open, setOpen] = useState(false);
    const [form] = Form.useForm();
    const { message } = App.useApp();
    // useMutation 사용
    const mutation = useMutation({
        mutationFn: async (values: EmployeeApiPayload) => {
            console.log(`API 호출 시작: POST /employee`);

            const response = await apiFetch(`/api/employee`, {
                method: "POST",
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    employees: [values]
                })
            });

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }

            console.log('API 응답 상태:', response.status);
            const data = await response.json();
            console.log('API 응답 데이터:', data);
            return data;
        },
        onSuccess: (data) => {
            console.log('성공:', data);
            message.success('저장되었습니다.');
            form.resetFields();
            onClose();
        },
        onError: (error) => {
            console.error('에러:', error);
        }
    });


    const showDrawer = () => {
        setOpen(true);
    }

    const onClose = () => {
        form.resetFields();
        setOpen(false);
    }

    const onFinish = async( values: EmployeeFormValues) => {
        const payload = {
            ...values,
            birYmd: values.birYmd?.format('YYYYMMDD'),
            empYmd: values.empYmd?.format('YYYYMMDD')
        };
        console.log('Form Vavlues' , payload);

        try{
            mutation.mutate(payload);
        }catch(e){
            console.log(e);
        }
    }

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
                            <Button type="primary" onClick={showDrawer}>
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
            <Drawer
                title="직원추가"
                placement="right"  // left, right, top, bottom
                onClose={onClose}
                open={open}
                width={400}  // 원하는 너비 설정
            >
                <div style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-between', height: '100%' }}>
                    <Form form={form} name="layout-multiple-horizontal" layout="vertical" onFinish={onFinish}>
                        <Form.Item
                            name="sabun"
                            label="사번"
                            rules={[{ required: true, message: '사번을 입력해주세요' }]}
                        >
                            <Input placeholder="사번을 입력해 주세요" />
                        </Form.Item>
                        <Form.Item layout="vertical" label="이름" name="name" rules={[{ required: true, message: '이름을 입력해주세요' }]}>
                            <Input placeholder="이름을 입력해 주세요"/>
                        </Form.Item>
                        <Form.Item layout="vertical" label="입사일" name="empYmd" rules={[{ required: true, message: '입사일을 입력해주세요' }]}>
                            <DatePicker
                                placeholder="입사일을 선택해 주세요"
                                style={{ width: '100%' }}  // 전체 너비로 설정
                            />
                        </Form.Item>
                        <Form.Item layout="vertical" label="생년월일" name="birYmd" rules={[{ required: true, message: '생년월일을 입력해주세요' }]}>
                            <DatePicker
                                placeholder="생년월일을 입력해 주세요"
                                style={{ width: '100%' }}  // 전체 너비로 설정
                            />
                        </Form.Item>
                        <Form.Item layout="vertical" label="메일" name="email" rules={[{ required: true, message: '메일을 입력해주세요' }]}>
                            <Input placeholder="e-mail을 입력해 주세요"/>
                        </Form.Item>
                        <Form.Item layout="vertical" label="주소" name="address" rules={[{ required: true, message: '주소를 입력해주세요' }]}>
                            <Input placeholder="주소를 입력해 주세요"/>
                        </Form.Item>
                    </Form>
                    <Space style={{ display: 'flex', justifyContent: 'flex-end', width: '100%' }}>
                        <Button onClick={onClose}>취소</Button>
                        <Button type="primary" onClick={() => form.submit()} loading={mutation.isPending}>
                            저장
                        </Button>
                    </Space>
                </div>
            </Drawer>
        </div>
    );
}

export default EmployeeListPage