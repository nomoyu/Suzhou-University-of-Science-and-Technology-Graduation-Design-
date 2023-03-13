import request from '@/utils/request'

// 查询论文详细信息列表
export function listPapers(query) {
  return request({
    url: '/business/papers/list',
    method: 'get',
    params: query
  })
}

// 查询论文详细信息详细
export function getPapers(id) {
  return request({
    url: '/business/papers/' + id,
    method: 'get'
  })
}

export function download(url){
  return request({
    url:'/download',
    method:"get",
    params:url
  })
}

// 新增论文详细信息
export function addPapers(data) {
  return request({
    url: '/business/papers',
    method: 'post',
    data: data
  })
}

// 修改论文详细信息
export function updatePapers(data) {
  return request({
    url: '/business/papers',
    method: 'put',
    data: data
  })
}

// 删除论文详细信息
export function delPapers(id) {
  return request({
    url: '/business/papers/' + id,
    method: 'delete'
  })
}
