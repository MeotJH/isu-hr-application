import React, { useCallback, useEffect, useRef, useState } from "react";
import { Table, Skeleton, Empty, Avatar } from "antd";
import type { TableProps } from "antd";
import { createAvatar } from '@dicebear/core';
import { personas } from '@dicebear/collection';
import {useNavigate} from "react-router-dom";

export interface EmpoloyeeListType {
    key: string;
    avatarSeed: string;
    name: string;
    organization: string;
}

// DiceBear 아바타 생성 함수
const generateAvatar = (seed: string) => {
    const avatar = createAvatar(personas, {
        seed,
        size: 40,
        backgroundColor: ["b6e3f4","c0aede","d1d4f9","ffd5dc","ffdfbf"]
    });
    return `data:image/svg+xml;utf8,${encodeURIComponent(avatar.toString())}`;
};

// 테이블 컬럼 정의
const columns: TableProps<EmpoloyeeListType>['columns'] = [
    {
        title: 'Image',
        dataIndex: 'avatarSeed',
        key: 'avatarSeed',
        width: '10%', // 1 비율
        render: (seed: string) => (
            <div style={{ textAlign: 'center' }}>
                <Avatar
                    src={generateAvatar(seed)}
                    size={40}
                    style={{ border: '1px solid #f0f0f0' }}
                />
            </div>
        )
    },
    {
        title: 'Name',
        dataIndex: 'name',
        key: 'name',
        width: '10%', // 1 비율
        render: (text) => <div style={{ textAlign: 'center' }}>{text}</div>
    },
    {
        title: 'Organization',
        dataIndex: 'organization',
        key: 'organization',
        width: '60%', // 3 비율
    }
];

type FetchResult = { rows: EmpoloyeeListType[]; hasMore: boolean };

// 내부 fetchPage 함수 (실제로는 API 호출로 대체될 예정)
const fetchEmployees = async (page: number, pageSize: number): Promise<FetchResult> => {
    // 실제로는 /api/employees?page=...&size=... 호출
    await new Promise(r => setTimeout(r, 300));
    const start = (page - 1) * pageSize;
    const totalPages = 100; // 예시
    const rows: EmpoloyeeListType[] = Array.from({ length: pageSize }, (_, i) => ({
        key: String(start + i + 1),
        avatarSeed: `user-${start + i + 1}`, // 고정 seed로 항상 같은 아바타 생성
        name: `User ${start + i + 1}`,
        organization: ["개발팀", "디자인팀", "마케팅팀", "영업팀", "인사팀"][(start + i) % 5],
    }));
    return { rows, hasMore: page < totalPages };
};

interface Props {
    pageSize?: number;
    emptyText?: React.ReactNode;
}

/** AntD Table + virtual + 무한 스크롤(IntersectionObserver) + 동적 높이(ResizeObserver) */
export default function EmployeeInfiniteTable({
                                                  pageSize = 100,
                                                  emptyText = "데이터가 없습니다.",
                                              }: Props) {
    const [data, setData] = useState<EmpoloyeeListType[]>([]);
    const [page, setPage] = useState(1);
    const [hasMore, setHasMore] = useState(true);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    // table 스크롤 높이를 동적으로 계산
    const containerRef = useRef<HTMLDivElement | null>(null);
    const [scrollY, setScrollY] = useState<number>(400); // 초기값

    useEffect(() => {
        if (!containerRef.current) return;
        const el = containerRef.current;
        const ro = new ResizeObserver(() => {
            // 컨테이너의 내부 높이를 그대로 Table scroll.y로 사용
            setScrollY(el.clientHeight);
        });
        ro.observe(el);
        setScrollY(el.clientHeight);
        return () => ro.disconnect();
    }, []);

    // IntersectionObserver: 테이블 body의 끝(Summary 내부 sentinel)을 root로 관찰
    const tableWrapRef = useRef<HTMLDivElement | null>(null);
    const sentinelRef = useRef<HTMLDivElement | null>(null);
    const ioRef = useRef<IntersectionObserver | null>(null);

    const load = useCallback(async () => {
        if (loading || !hasMore) return;
        setLoading(true);
        try {
            const { rows, hasMore: next } = await fetchEmployees(page, pageSize);
            setData(prev => [...prev, ...rows]);
            setHasMore(next);
            setPage(p => p + 1);
        } finally {
            setLoading(false);
        }
    }, [loading, hasMore, page, pageSize]);

    // 초기 로드
    useEffect(() => { load(); /* eslint-disable-next-line */ }, []);

    // IO 세팅
    useEffect(() => {
        const wrap = tableWrapRef.current;
        if (!wrap || !sentinelRef.current) return;

        const scrollRoot =
            (wrap.querySelector(".ant-table-body") as HTMLElement) ||
            (wrap.querySelector(".ant-table-container .ant-table-body") as HTMLElement);

        if (!scrollRoot) return;

        if (ioRef.current) {
            ioRef.current.disconnect();
            ioRef.current = null;
        }

        const io = new IntersectionObserver(
            entries => {
                const e = entries[0];
                if (e.isIntersecting) load();
            },
            { root: scrollRoot, rootMargin: "0px 0px 200px 0px", threshold: 0 }
        );
        io.observe(sentinelRef.current);
        ioRef.current = io;

        return () => {
            io.disconnect();
            ioRef.current = null;
        };
    }, [data.length, hasMore, load]);

    return (
        <div ref={containerRef} style={{ height: "100%", display: "flex", minHeight: 0 }}>
            <div ref={tableWrapRef} style={{ flex: 1, minHeight: 0 }}>
                <Table<EmpoloyeeListType>
                    virtual
                    pagination={false}
                    rowKey="key"
                    dataSource={data}
                    columns={columns}
                    scroll={{ y: scrollY }}
                    locale={{ emptyText }}
                    onRow={(record) => ({
                        onClick: () => {
                            console.info(record,"record");
                            navigate(`/employee`);
                        },
                        style:{cursor:'pointer'}
                    })}
                    summary={() => (
                        <Table.Summary>
                            <Table.Summary.Row>
                                <Table.Summary.Cell index={0} colSpan={columns?.length || 1}>
                                    {/* sentinel: 테이블 body의 최하단에 위치 */}
                                    <div ref={sentinelRef} style={{ height: 1 }} />
                                    <div style={{ padding: "8px 16px" }}>
                                        {loading ? (
                                            <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
                                                {Array.from({ length: 5 }).map((_, i) => (
                                                    <div
                                                        key={i}
                                                        style={{
                                                            display: 'flex',
                                                            alignItems: 'center',
                                                            height: '47px', // 테이블 row 높이와 동일
                                                            padding: '12px 16px',
                                                            borderBottom: '1px solid #f0f0f0',
                                                            gap: '16px'
                                                        }}
                                                    >
                                                        <div style={{ flex: '0 0 20%' }}>
                                                            <Skeleton.Input
                                                                active
                                                                size="small"
                                                                style={{ width: '100%' }}
                                                            />
                                                        </div>
                                                        <div style={{ flex: '0 0 20%' }}>
                                                            <Skeleton.Input
                                                                active
                                                                size="small"
                                                                style={{ width: '100%' }}
                                                            />
                                                        </div>
                                                        <div style={{ flex: '0 0 60%' }}>
                                                            <Skeleton.Input
                                                                active
                                                                size="small"
                                                                style={{ width: '100%' }}
                                                            />
                                                        </div>
                                                    </div>
                                                ))}
                                            </div>
                                        ) : hasMore ? "" : (
                                            <div style={{ textAlign: "center", padding: 8 }}>
                                                <Empty
                                                    image={Empty.PRESENTED_IMAGE_SIMPLE}
                                                    description="모든 데이터 로드 완료"
                                                />
                                            </div>
                                        )}
                                    </div>
                                </Table.Summary.Cell>
                            </Table.Summary.Row>
                        </Table.Summary>
                    )}
                />
            </div>
        </div>
    );
}

