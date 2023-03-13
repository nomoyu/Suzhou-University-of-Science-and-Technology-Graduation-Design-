<template>
  <div ref="myChart" id="myChart"></div>
</template>

<script>
import * as echarts from "echarts"
export default {
    mounted(){
        let myEcharts = echarts.init(this.$refs.myChart)
        let options = {
            title:{
                text:"漏斗图"
            },
            tooltip:{
                trigger:"item",
                formatter:"{a}<br/>{b}:{c}%"
            },
            series:[
                {
                    type:"funnel",
                    data:[
                    { value: 60, name: 'Visit' },
                    { value: 40, name: 'Inquiry' },
                    { value: 20, name: 'Order' },
                    { value: 80, name: 'Click' },
                    { value: 100, name: 'Show' }
                    ],
                    left:'10%',
                    sort:"ascending",  //排序
                    // sort:"none",
                    itemStyle:{
                        borderColor:"red",
                        borderWidth:3
                    },
                    label:{
                        show:true,
                        position:"inside"
                    },
                    emphasis:{
                        label:{
                            fontSize:30
                        }
                    }
                }
            ]
        }
        myEcharts.setOption(options)
    }
}
</script>

<style>

</style>