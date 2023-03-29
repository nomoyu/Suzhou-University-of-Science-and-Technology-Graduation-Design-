<template>
    <div
        :class="{'has-logo':showLogo}"
        :style="{ backgroundColor: settings.sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground }"
        >
        <logo v-if="showLogo" :collapse="isCollapse" />
        <el-scrollbar
            id="leftmenu"
            :class="settings.sideTheme"
            v-resize="MuneResize"
            wrap-class="scrollbar-wrapper">
            <el-menu
                :default-active="activeMenu"
                :collapse="isCollapse"
                :background-color="settings.sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground"
                :text-color="settings.sideTheme === 'theme-dark' ? variables.menuColor : variables.menuLightColor"
                :unique-opened="true"
                :active-text-color="settings.theme"
                :collapse-transition="false"
                mode="vertical"
            >
                <sidebar-item
                    v-for="(route, index) in sidebarRouters"
                    :key="route.path  + index"
                    :item="route"
                    :base-path="route.path"
                />
            </el-menu>
        </el-scrollbar>
        <!-- 给个可以拖拽的标识 -->
        <div id="drap-meuline" style="left:197px"/>
    </div>
</template>

<script>
import { mapGetters, mapState } from "vuex";
import Logo from "./Logo";
import SidebarItem from "./SidebarItem";
import variables from "@/assets/styles/variables.scss";

export default {
    components: { SidebarItem, Logo },
    computed: {
        ...mapState(["settings"]),
        ...mapGetters(["sidebarRouters", "sidebar"]),
        activeMenu() {
            const route = this.$route;
            const { meta, path } = route;
            // if set path, the sidebar will highlight the path you set
            if (meta.activeMenu) {
                return meta.activeMenu;
            }
            return path;
        },
        showLogo() {
            return this.$store.state.settings.sidebarLogo;
        },
        variables() {
            return variables;
        },
        isCollapse() {
            return !this.sidebar.opened;
        }
    },

     // 自定义指令 获取当前菜单的宽度
    directives: {
        // 指令的名称
        resize: {
        // el为绑定的元素，binding为绑定给指令的对象
        bind(el, binding) {
            let _width = "";
            function isReize() {
            const style = document.defaultView.getComputedStyle(el);
            if (_width !== style.width) {
                binding.value({ width: style.width, targetId: el.id });
                _width = style.width;
            }
            }
            el.__vueSetInterval__ = setInterval(isReize, 300);
        },
        unbind(el) {
            clearInterval(el.__vueSetInterval__);
        },
        },
    },
    mounted() {
        // 获取右侧内容Dom
        var drapLine = document.getElementById("drap-meuline");
        // 获取左侧菜单Dom
        var mainContainer = document.getElementsByClassName("main-container")[0];
        // 获取左侧菜单Dom父元素，为了动态设置宽度
        var menuleft = document.getElementById("leftmenu");
        var sidebarWidth = document.getElementsByClassName("sidebar-container")[0];

        // 是否需要本地保存
        // const history_width = localStorage.getItem("sliderWidth");
        // if (history_width) {
        //   sidebarWidth.style.width = history_width;
        //   mainContainer.style.marginLeft = history_width;
        // }
        drapLine.onmousedown = function (e) {
            // 设置最大/最小宽度
            var max_width = 600;
            var min_width = 100;
            let mouse_x = 0; // 记录鼠标相对left盒子x轴位置
            e.preventDefault(); // 阻止默认事件
            const _e = e || window.event;
            mouse_x = _e.clientX - menuleft.offsetWidth;
            document.onmousemove = function (e_) {
                // console.log(min_width, max_width);
                const _e_ = e_ || window.event;
                let left_width = _e_.clientX - mouse_x;
                left_width = left_width < min_width ? min_width : left_width;
                left_width = left_width > max_width ? max_width : left_width;
                sidebarWidth.style.width = left_width + "px";
                mainContainer.style.marginLeft = left_width + "px";
        };

        document.onmouseup = function (e) {
            let drapLine_width = sidebarWidth.style.width
            // console.log("qian"+drapLine.style.left);
            drapLine.style.left = Number(drapLine_width.slice(0,-2))-3 + "px"
            // console.log(Number(drapLine_width.slice(0,-2)));
            document.onmousemove = null;
            document.onmouseup = null;
            // 本地保存
            // localStorage.setItem("sliderWidth", menuleft.style.width);
        };
        };
    },

    //拖拽宽度的改变
    methods:{
    // 动态获取左侧菜单的宽度
    MuneResize(data) {
        // 拿到左侧菜单父元素
        const leftDom = document.getElementById(`${data.targetId}`);
        // 拿到右侧内容父元素
        const mainContainer =
        document.getElementsByClassName("main-container")[0];
        mainContainer.style.marginLeft = leftDom.clientWidth + "px";
        },
    }

};
</script>
<style lang="scss">
    .el-submenu__title,.el-menu-item{
        height: 50px;
        line-height: 50px;
        font-size: 11px;
    }
    #drap-meuline {
        background: transparent;
        width: 4px;
        height: 100%;
        position:absolute;
        top:0px;
        z-index: 10;
        cursor: e-resize; //设置鼠标悬浮上去显示可拖拽样式
    }

</style>