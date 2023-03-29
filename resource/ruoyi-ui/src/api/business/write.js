import request from '@/utils/request'

// 查询论文详细信息列表
export function add(data) {
  return request({
    url: '/business/write',
    method: 'post',
    data: data
  })
}

export function list(data) {
  return request({
    url: '/business/write/list',
    method: 'get',
    params: data
  })
}
