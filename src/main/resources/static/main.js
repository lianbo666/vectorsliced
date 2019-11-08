var mp_obj=(function (){
    return{
        //历史数据
        histList:[{
            dynasty:'夏朝',
            s_year:-2070,
            t_span:470
        },{
            dynasty:'商朝',
            s_year:-1600,
            t_span:554
        },{
            dynasty:'西周',
            s_year:-1046,
            t_span:276
        },{
            dynasty:'春秋战国',
            s_year:-770,
            t_span:549
        },{
            dynasty:'秦朝',
            s_year:-221,
            t_span:19
        },{
            dynasty:'汉朝',
            s_year:-202,
            t_span:386
        },{
            dynasty:'三国两晋南北朝',
            s_year:184,
            t_span:397
        },{
            dynasty:'隋朝',
            s_year:581,
            t_span:37
        },{
            dynasty:'唐朝',
            s_year:618,
            t_span:289
        },{
            dynasty:'五代十国',
            s_year:907,
            t_span:53
        },{
            dynasty:'元朝',
            s_year:1271,
            t_span:301
        },{
            dynasty:'元朝',
            s_year:1271,
            t_span:301
        },{
            dynasty:'明朝',
            s_year:1368,
            t_span:301
        },{
            dynasty:'清朝',
            s_year:1636,
            t_span:274
        }],
        //初始化
        init:function(maps){
            this.axisInit();
            this.mouseInit(maps);
        },
        //通过clientX计算下标
        getIndex:function(clinetX,width,step,maps){
            var num_ = (clinetX - 10) < 0 ? 10 : clinetX;
            var index = Math.abs(parseInt((num_-10-5)/width));
            //控制时间轴的颜色的变化
            document.querySelectorAll('.map-axis').forEach(function (i,v) {
                if(v == index){
                    i.style.border='3px solid #f39624';
                }else{
                    i.style.border='3px solid #fce8d0';
                }
            })

            //时间轴下方文字颜色的改变
            var lis =  document.getElementsByClassName('map-Dline')
            for(var i = 0; i < lis.length; i++){
                var texts = lis[i].children[1].innerText
                //console.log(texts)
                if(i == index){
                    lis[i].children[1].children[0].style.color = '#de7f11'
                }else{
                    lis[i].children[1].children[0].style.color = '#545454'
                }

            }

            num_ = num_ - (width * index);
            if(index >= this.histList.length){
                index = this.histList.length-1;
                num_ = width + 15;
            }
            var year_;
            if(this.histList[index].s_year<0){
                year_ = Math.ceil(this.histList[index].s_year + this.histList[index].t_span/width/step*((num_-15)<0?0:(num_-15)))
            }
            if(this.histList[index].s_year+this.histList[index].t_span/width/step*(num_-15)>=0){
                year_ = Math.floor(this.histList[index].s_year + this.histList[index].t_span/width/step*((num_-15)<0?0:(num_-15)))
            }
            this.success(maps,year_);
            // console.group()
            // console.log('clinetX:',num_)
            // console.log('num:',clinetX-15)
            // console.log('width:',width)
            // console.log('step:',step)
            // console.log('index:',index)
            // console.log('length:',this.histList[index].t_span/width/step*(num_-15))
            // console.log('year:',this.histList[index].s_year)
            // console.log('year_:',year_)
            // console.log('t_span:',this.histList[index].t_span)
            // console.log('t_speed:',this.histList[index].t_span/width/step)
            // console.groupEnd()
            return {
                t_span:this.histList[index].t_span,
                t_speed:this.histList[index].t_span/width/step,
                t_year:year_
            }
        },
        success:function(map,year_){
         //map.on('load', function loaded() {
            if(map.isSourceLoaded('province-polygon-source')){
                /*func1 使用原始的添加和删除layer的方法实现图层的重载入*/
                //删除layer和resource
                map.removeLayer('province-polygon-vector-layer');
                map.removeLayer('province-polygon-symbol-layer');

                map.removeSource('province-polygon-source');
                //重新添加layer和resource
                map.addSource('province-polygon-source', {
                    type: 'vector',
                    tiles: ['http://localhost:8080/polygons/'+year_+'/province/{z}/{x}/{y}']
                });
                map.addLayer({
                    //id全局唯一
                    "id": "province-polygon-vector-layer",
                    "type": "fill",
                    "source": "province-polygon-source",
                    "source-layer": "province",
                    "maxzoom": 9,
                    //"filter": ["==", "beg_yr", 188],
                    "layout": {},
                    "paint": {
                        "fill-outline-color": "#ff0000",
                        "fill-antialias": true,
                        //"fill-outline-width": 3,
                        "fill-color": ['get', 'color'],
                        "fill-opacity": {
                            "stops": [
                                [5, 0.8],
                                [6, 0.5],
                                [7, 0.3],
                                [8, 0.2]
                            ]
                        }
                    }
                });
                map.addLayer({
                    "id": "province-polygon-symbol-layer",
                    "type": "symbol",
                    "source": "province-polygon-source",
                    "source-layer": "province",
                    "maxzoom": 9,
                    "layout": {
                        "text-line-height": 1.1,
                        "text-size": 16,
                        "text-offset": [0, 1.5],
                        "symbol-placement": "point",
                        "symbol-avoid-edges": true,
                        //"text-field": "{name_ch}",
                        "text-field": [
                            "format",
                            ["get", "name_ch"], {"font-scale": 0.8},
                            "-", {},
                            ["get", "name_py"], {"font-scale": 0.6}
                        ],
                        "text-letter-spacing": 0.1,
                        "text-justify": "auto",
                        "text-max-width": 5000
                    },
                    "paint": {
                        "text-color": "#00f",
                        "text-halo-width": 1.25,
                        "text-opacity": 1
                    }
                });
                /*func2 使用内置的过滤方法实现图层的变换*/
            }
           // });
        },
        mapInit:function(){
            mapboxgl.accessToken = 'pk.eyJ1IjoicGFuamlhbndlbiIsImEiOiJjamh2Y2QyOHcweHMzM3F0YzZxbXp2MHZxIn0.GXyyIbdELQWVH93B27FbnQ';
            var tileset = 'mapbox.streets';
            var map = new mapboxgl.Map({
                container: 'map',
                zoom: 6,
                center: [114.883679,27.631216],
                // style: 'mapbox://styles/mapbox/light-v9',
                // style: '/mapconfig.json',
                style: '/map-style.json',
                hash: false
            });
            map.on('load', function loaded() {
                /* 初始化使用-2070年*/
                map.addSource('province-polygon-source', {
                    type: 'vector',
                    tiles: ['http://localhost:8080/polygons/-2070/province/{z}/{x}/{y}']
                });
                map.addLayer({
                    //id全局唯一
                    "id": "province-polygon-vector-layer",
                    "type": "fill",
                    "source": "province-polygon-source",
                    "source-layer": "province",
                    "maxzoom": 9,
                    //"filter": ["==", "beg_yr", 188],
                    "layout": {},
                    "paint": {
                        "fill-outline-color": "#ff0000",
                        "fill-antialias": true,
                        //"fill-outline-width": 3,
                        "fill-color": ['get', 'color'],
                        "fill-opacity": {
                            "stops": [
                                [5, 0.8],
                                [6, 0.5],
                                [7, 0.3],
                                [8, 0.2]
                            ]
                        }
                    }
                });
                map.addLayer({
                    "id": "province-polygon-symbol-layer",
                    "type": "symbol",
                    "source": "province-polygon-source",
                    "source-layer": "province",
                    "maxzoom": 9,
                    "layout": {
                        "text-line-height": 1.1,
                        "text-size": 16,
                        "text-offset": [0, 1.5],
                        "symbol-placement": "point",
                        "symbol-avoid-edges": true,
                        //"text-field": "{name_ch}",
                        "text-field": [
                            "format",
                            ["get", "name_ch"], {"font-scale": 0.8},
                            "-", {},
                            ["get", "name_py"], {"font-scale": 0.6}
                        ],
                        "text-letter-spacing": 0.1,
                        "text-justify": "auto",
                        "text-max-width": 5000
                    },
                    "paint": {
                        "text-color": "#00f",
                        "text-halo-width": 1.25,
                        "text-opacity": 1
                    }
                });
            });
            this.init(map);
        },
        mouseInit:function(maps){
            var step_ = 1;
            var this_ = this;
            //鼠标初始化
            //鼠标一直按下事件
            var iskeyDown=false;
            //起始x值
            var startX = 0;
            // 移动值
            var moveX = 0;
            // 总的移动距离
            var distanceX = 0
            //获取父级元素和属性
            var s_boxNode = document.querySelector('.map-timeLine');
            var s_boxNode_width = s_boxNode.offsetWidth
            // var S_boxNode=width2  = s_boxNode.Width;
            //获取子级元素(滑块)和属性
            var s_moveNode = document.querySelector('.map-scroll');
            var s_moveNode_width = s_moveNode.offsetWidth;
            //获取标签
            var s_year = document.querySelector('#year');
            //活动范围
            var maxDistance = s_boxNode_width + s_moveNode_width;
            //鼠标开始
            s_boxNode.addEventListener('mousedown',function (e) {
                iskeyDown = true;
                distanceX = e.clientX - (s_moveNode.offsetWidth + s_moveNode.offsetWidth / 2);
                if(iskeyDown){
                    if(distanceX < 0){
                        distanceX = 0
                    }
                    if(distanceX > s_boxNode_width){
                        distanceX = s_boxNode_width - s_moveNode.offsetWidth / 2
                    }
                    s_year.value=this_.getIndex(e.clientX,document.querySelector('.map-Dline').offsetWidth,step_,maps).t_year;
                    s_moveNode.style.transform = 'translateX(' + distanceX + 'px)';
                }
            });
            //鼠标移动事件
            s_boxNode.addEventListener('mousemove',function (e) {
                distanceX = e.clientX - (s_moveNode.offsetWidth + s_moveNode.offsetWidth/2);
                if(iskeyDown){
                    if(distanceX < 0){
                        distanceX = 0
                    }
                    if(distanceX > s_boxNode_width){
                        distanceX = s_boxNode_width - s_moveNode.offsetWidth/2
                    }
                    s_year.value=this_.getIndex(e.clientX,document.querySelector('.map-Dline').offsetWidth,step_,maps).t_year;
                    s_moveNode.style.transform = 'translateX(' + distanceX + 'px)';
                }
            });
            //鼠标离开元素
            window.addEventListener('mouseup',function(e){
                if(e.target != s_moveNode){
                    iskeyDown = false;
                }
            });
            //鼠标结束移动事件
            s_boxNode.addEventListener('mouseup',function (e) {
                iskeyDown=false;
            });
        },
        axisInit:function(){
            //坐标轴初始化
            var s_boxNode = document.querySelector('.map-timeLine');
            var str='';

            for (var key in this.histList) {
                str +='<li class="map-Dline">'+
                    '<div class="map-axis"></div>'+
                    '<div>';
                if(key==0){
                    str +='<div style="color: rgb(222, 127, 17)">'+(this.histList[key].dynasty)+'</div>';
                } else{
                    str +='<div>'+(this.histList[key].dynasty)+'</div>';
                }
                str +='<div>'+(this.histList[key].s_year<0?'BC'+(Math.abs(this.histList[key].s_year)):this.histList[key].s_year)+'</div>'+
                    '</div>'+
                    '</li>';
            }
            str+='<span class="map-scroll"></span>';
            s_boxNode.innerHTML=str;

        },
        //resize事件反向计算位置
        reSizeEvent:function(){
            //获取时间
            var s_year = document.querySelector('#year');

            // let arr1 = this.histList.findIndex((value,index,array) =>value.s_year<)

            var s_boxNode = document.querySelector('.map-timeLine');
        }
    }
})();
// mp_obj.mapInit();