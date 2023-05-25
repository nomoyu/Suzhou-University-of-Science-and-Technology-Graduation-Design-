import request from '@/utils/request'

// 查询论文详细信息列表
export function model(data) {
  return request({
    url: '/python/model/'+ data,
    method: 'get',
  })
}

export function processFile(data) {
  return request({
    url: '/python/process_file/'+ data,
    method: 'post',
    headers: {
    'Content-Type': 'multipart/form-data'
  }
  })
}

