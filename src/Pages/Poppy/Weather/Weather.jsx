import React from 'react';
import './Weather.css';
import sun from '../../../Images/sunsiz.png'
import image1 from '../../../Images/Rectangle 947.png'
import image2 from '../../../Images/Rectangle 948.png'
import image3 from '../../../Images/Rectangle 949.png'
import image4 from '../../../Images/a1.png'
import image5 from '../../../Images/a2.png'
import image6 from '../../../Images/a3.png'
import left from '../../../Images/Group 185.png'
import sun1 from '../../../Images/sun1.png'
import nhietdo from '../../../Images/Ellipse 52.png'
import d from '../../../Images/Ellipse 58.png';
import c from '../../../Images/C.png'
import f from '../../../Images/F.png'
import Slick from '../Slick/Slick';
import sd1 from '../../../Images/Rectangle 1014.png';
import sd2 from '../../../Images/Rectangle 1017.png';
import sd3 from '../../../Images/Rectangle 1019.png';


////////////////////////////////////////////// có api rồi thì xóa này đi 
const ListWeather = [
    {
        img : image1,
        des : "Trải nghiệm văn hóa tại 10 quán cà phê đẹp ở Hà Nội "
    },
    {
        img : image2,
        des : "Top 8 quán ăn đêm Hà Nội dành cho hội cú đêm "
    },
    {
        img : image3,
        des : "Top 10 quán chè ngon bạn phải thử một lần"
    },
]
const ListWeather2 = [
    {
        img : image4,
        des : "Hoàng hôn nghĩa là gì ? 6+ý nghĩa kì diệu của hoàng hôn  "
    },
    {
        img : image5,
        des : "Hôm nay mưa dông diễn ra ở nhiều khu vực trên cả nước "
    },
    {
        img : image6,
        des : "Tin thời tiết ba miền những ngày cuối tháng 7, miền Nam bước vào đợt mưa giông dài trong tối nay "
    },
]

// hiển thị các tỉnh lân cận
const ListWeather3 = [
    {
        img : sun,
        title : "Vĩnh Phúc",
        nd1:"25",
        nd2:"25",
        des:"Mây rải rác"
    },
    {
        img : sun,
        title : "Vĩnh Phúc",
        nd1:"25",
        nd2:"25",
        des:"Mây rải rác"
    },
    {
        img : sun,
        title : "Vĩnh Phúc",
        nd1:"25",
        nd2:"25",
        des:"Mây rải rác"
    },
    {
        img : sun,
        title : "Vĩnh Phúc",
        nd1:"25",
        nd2:"25",
        des:"Mây rải rác"
    },
    {
        img : sun,
        title : "Vĩnh Phúc",
        nd1:"25",
        nd2:"25",
        des:"Mây rải rác"
    },
    {
        img : sun,
        title : "Vĩnh Phúc",
        nd1:"25",
        nd2:"25",
        des:"Mây rải rác"
    },
    {
        img : sun,
        title : "Vĩnh Phúc",
        nd1:"25",
        nd2:"25",
        des:"Mây rải rác"
    },
    {
        img : sun,
        title : "Vĩnh Phúc",
        nd1:"25",
        nd2:"25",
        des:"Mây rải rác"
    },
    {
        img : sun,
        title : "Vĩnh Phúc",
        nd1:"25",
        nd2:"25",
        des:"Mây rải rác"
    },
]

// sử dụng để hiển thị dự báo thời tiết demo  của ngày hôm nay 
const dbWeatherToday = {
    address:"Dự báo thời tiết Hà Nội",
    update : "11",
    status : "Mây cụm",
    LikeTemperature : "Cảm giác như 33",
    temperature : "33"
}
const overview = {
    sunrise : "21",
    sunset :"21",
    hight : "19",
    short : "19",
    humidity : "70",
    Pressure : "1010mb",
    vision:"10km",
    wind : "3.6km/h",
    pointStop : "23",
    UV : "0"
}

const ListCity = [
    {
        city : "Văn Định",
        address : "Thị trấn Văn, Huyện Ứng Hòa",
        rain : "0",
        status : "Không mưa"
    },
    {
        city : "Văn Định",
        address : "Thị trấn Văn, Huyện Ứng Hòa",
        rain : "0",
        status : "Không mưa"
    },
    {
        city : "Văn Định",
        address : "Thị trấn Văn, Huyện Ứng Hòa",
        rain : "0",
        status : "Không mưa"
    },
    {
        city : "Văn Định",
        address : "Thị trấn Văn, Huyện Ứng Hòa",
        rain : "0",
        status : "Không mưa"
    },
]

const quanlityAir = {
    co1 : "357.15",
    co2 : "257.21",
    nh3 : "2.32",
    no : "0.27",
    no2 : "5.32",
    o3 : "74.23",
    pm25 : "123.12",
    so2 : "2.41" 
}




//////////////////////////////////////////////
const Weather = () => {
    return (
        <div className='container mt-3'>
            <div className="row justify-content-between">
                <div className="col-left">
                    <div className="col_LeftItem1 mb-4">
                        <div className="col_LeftItem1_hearder">
                            <p>{dbWeatherToday.address}</p>
                            <small>Đã cập nhật {dbWeatherToday.update} phút trước</small>
                        </div>
                        <div className="col_LeftItem1_content">
                            <div className="d-flex align-center mb-2 mt-2">
                                <img src={sun1} alt=""/>
                                <h1 style={{fontSize:"4.2rem",color:"white",marginLeft:"12px"}}>{dbWeatherToday.temperature}</h1>
                                <img style={{marginRight:"6px"}} width={"30px"} height={"30px"} src={nhietdo} alt="" />
                                <div>
                                    <img style={{display:"block",marginBottom:"6px"}} width={"25px"} height={"30px"} src={c} alt="" />
                                    <img style={{display:"block"}} width={"25px"} height={"30px"} src={f} alt="" />
                                </div>
                                <div className="col_LeftItem1_content_items">
                                    <h1>{dbWeatherToday.status}</h1>
                                    <p>{dbWeatherToday.LikeTemperature}</p>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-2">
                                    <p>Thấp/Cao</p>
                                    <p>{overview.short}/{overview.hight}</p>
                                </div>
                                <div className="col-2">
                                    <p>Độ ấm</p>
                                    <p>{overview.Pressure}</p>
                                </div>
                                <div className="col-2">
                                    <p>Tầm nhìn</p>
                                    <p>{overview.vision}</p>
                                </div>
                                <div className="col-2">
                                    <p>Gió</p>
                                    <p>{overview.wind}</p>
                                </div>
                                <div className="col-2">
                                    <p>Điểm ngưng</p>
                                    <p>{overview.pointStop}</p>
                                </div>
                                <div className="col-2">
                                    <p>Chỉ số UV</p>
                                    <p style={{fontSize:"20px"}}>{overview.UV}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="col_LeftItem2 mb-4">
                        <div className="col_header_LeftItem2 d-flex justify-content-between align-items-center">
                            <h5>Lượng mưa Hà Nội hôm nay</h5>
                            <p>Lượng mưa ở địa điểm khác</p>
                        </div>
                        <div className="col_content_LeftItem2 d-flex justify-content-center align-items-center">
                            <div className="round1">
                                <div className="round2">
                                    <div className="">
                                        <p>0</p>
                                        <p>mm</p>
                                    </div>
                                </div>
                            </div>
                            <h6>Đo tại Bất Đạt</h6>
                        </div>
                        <div className="list_rain">
                            {
                                ListCity.map((e,index)=>(
                                    <div className="rol_item2 row col_list_LeftItem2 justify-content-between">
                                        <div className="col-10">
                                            <p>{e.city}</p>
                                            <p>{e.address}</p>
                                        </div>
                                        <div className="col-2" >
                                            <p>{e.rain} mm</p>
                                            <p>{e.status}</p>
                                        </div>
                                    </div>
                                ))
                            }
                        </div>
                    </div>
                    <div className="col_LeftItem3 mb-2">
                        <div className="col_LeftItem3_header d-flex justify-content-between align-items-center">
                            <h5>Chất lượng không khí : Khá</h5>
                            <div className="list_badges">
                                <span>Tốt</span>
                                <span>Khá</span>
                                <span>Trung bình</span>
                                <span>Kém</span>
                                <span>Rất kém</span>
                            </div>
                        </div>
                        <div className="list_khihau">
                            <div className="row row_khihau" >
                                <div className="col col_khihau">
                                    <div className="">
                                        <p>CO</p>
                                        <p>{quanlityAir.co1}</p>
                                    </div>
                                </div>
                                <div className="col">
                                    <div className="">
                                        <p>CO</p>
                                        <p>{quanlityAir.co2}</p>
                                    </div>
                                </div>
                                <div className="col">
                                    <div className="">
                                        <p>NH<sub>3</sub></p>
                                        <p>{quanlityAir.nh3}</p>
                                    </div>
                                </div>
                                <div className="col col_khihau">
                                    <div className="">
                                        <p>NO</p>
                                        <p>{quanlityAir.no}</p>
                                    </div>
                                </div>
                                <div className="col">
                                    <div className="">
                                        <p>NO<sub>2</sub></p>
                                        <p>{quanlityAir.no2}</p>
                                    </div>
                                </div>
                                <div className="col">
                                    <div className="">
                                        <p>O<sub>3</sub></p>
                                        <p>{quanlityAir.o3}</p>
                                    </div>
                                </div>
                                <div className="col col_khihau">
                                    <div className="">
                                        <p>PM <sub>25</sub></p>
                                        <p>{quanlityAir.pm25}</p>
                                    </div>
                                </div>
                                <div className="col">
                                    <div className="">
                                        <p>SO<sub>2</sub></p>
                                        <p>{quanlityAir.so2}</p>
                                    </div>
                                </div>
                                
                            </div>
                        </div>
                    </div>
                    <div className="col_LeftItem4 mb-2">
                        <p>Thời tiết Hà Nội theo giờ ( 24h )</p>
                    </div>
                    <div className="col_LeftItem5 mb-4" style={{height:"200px"}}>
                        <Slick/>
                    </div>
                    <div className="col_LeftItem4 mb-4">
                        <p>Nhiệt độ và khả năng có mưa Hà Nội trong 12h tới</p>
                    </div>
                    <div className="col_LeftItem mb-4">
                        <img src={sd1} alt="" width="518px" />
                        <div className="list_day d-flex justify-content-between pt-4">
                            <div className="item_day">
                                <p>Ngày mai</p>
                            </div>
                            <div className="item_day">
                                <p>3 ngày</p>
                            </div>
                            <div className="item_day">
                                <p>5 ngày</p>
                            </div>
                            <div className="item_day">
                                <p>7 ngày</p>
                            </div>
                            <div className="item_day">
                                <p>10 ngày</p>
                            </div>
                            <div className="item_day">
                                <p>15 ngày</p>
                            </div>
                            <div className="item_day">
                                <p>30 ngày</p>
                            </div>
                            
                        </div>
                    </div>
                    <div className="col_LeftItem5 mb-4" style={{height:"200px"}}>
                        <Slick/>
                    </div>
                    <div className="col_LeftItem mb-4">
                        <img src={sd2} alt="" width="518px" />
                    </div>
                    <div className="col_LeftItem mb-4">
                        <img src={sd3} alt="" width="518px" />
                    </div>
                </div>
                <div className="col-right">
                    <div className="col_item1">
                        <div className="title_new_item1 mb-4">
                            <p style={{margin:"0",padding:"8px 0 8px 8px"}}>Tin tức Hà Nội</p>
                            {
                                ListWeather.map((e,index)=>(
                                    <div className="row row_itemNew" style={{padding:"8px 8px"}}>
                                        <div className="col-3">
                                            <img width={"100%"}  src={e.img} alt="" />
                                        </div>
                                        <div className="col-9" style={{paddingLeft:"6px"}}>
                                            <p style={{fontSize:"12px"}}>{e.des}</p>
                                        </div>
                                    </div>
                                ))
                            }
                        </div>

                        <div className="title_new_item2 mb-4">
                            <p style={{margin:"0",padding:"8px 0 8px 8px"}}>Thời tiết các tỉnh lân cận</p>
                            {
                                ListWeather3.map((e,index)=>(
                                    <div key={index} className='row row_tinh' style={{padding:"6px 0 6px 4px"}}>
                                        <div className="col-2">
                                            <img src={e.img} alt="" />
                                        </div>
                                        <div className="col-6">
                                            <span>{e.title}</span>
                                        </div>
                                        <div className="col-4">
                                            <div className="d-flex justify-content-center align-items-center" style={{fontSize:"12px"}}>
                                                <div style={{ position: "relative" }}>
                                                    <span style={{padding:"0 2px"}}>{e.nd1}</span>
                                                    <img style={{ position: "absolute", top: 5 }} src={d} alt="" />
                                                </div>
                                                <span style={{ margin: "0 4px 0 8px" }}>/</span>
                                                <div style={{ position: "relative" }}>
                                                    <span style={{padding:"0 2px"}}>{e.nd2}</span>
                                                    <img style={{ position: "absolute", top: 5 }} src={d} alt="" />
                                                </div>
                                            </div>
                                            <p style={{ textAlign:"center",fontSize:"12px"}}>{e.des}</p>
                                        </div>
                                    </div>
                                ))
                            }
                        </div>

                        <div className="title_new_item3 mb-4">
                            <div className="header_title_new_item3">
                                <img src={left} alt="" />
                            </div>
                            <div className="row">
                                <div className="col-6">
                                    <ul className='list_groups'>
                                        <li><a href="#">Huyện Ba Vì</a></li>
                                        <li><a href="#">Huyện Chương Mỹ</a></li>
                                        <li><a href="#">Huyện Đan Phượng</a></li>
                                        <li><a href="#">Huyện Đông Anh</a></li>
                                        <li><a href="#">Huyện Gia Lâm</a></li>
                                        <li><a href="#">Huyện Hoài Đức</a></li>
                                        <li><a href="#">Huyện Mê Linh</a></li>
                                        <li><a href="#">Huyện Mỹ Đức</a></li>
                                        <li><a href="#">Huyện Phú Xuyên</a></li>
                                        <li><a href="#">Huyện Phúc Thọ</a></li>
                                        <li><a href="#">Huyện Quốc Oai</a></li>
                                        <li><a href="#">Huyện Sóc Sơn</a></li>
                                        <li><a href="#">Huyện Thạch Thất</a></li>
                                        <li><a href="#">Huyện Thanh Oai</a></li>
                                        <li><a href="#">Huyện Thanh Trì</a></li>
                                    </ul>
                                </div>
                                <div className="col-6">
                                    <ul className='list_groups'>
                                        <li><a href="#">Huyện Thường Tín</a></li>
                                        <li><a href="#">Huyện Ứng Hoa</a></li>
                                        <li><a href="#">Quận Ba Đình</a></li>
                                        <li><a href="#">Quận Bắc Từ Liêm</a></li>
                                        <li><a href="#">Quận Cầu Giấy</a></li>
                                        <li><a href="#">Quận Đống Đa</a></li>
                                        <li><a href="#">Quận Hà Đông</a></li>
                                        <li><a href="#">Quận Hai Bà Trưng</a></li>
                                        <li><a href="#">Quận Hoàn Kiếm</a></li>
                                        <li><a href="#">Quận Hoàng Mai</a></li>
                                        <li><a href="#">Quận Long Biên</a></li>
                                        <li><a href="#">Quận Nam Từ Liêm</a></li>
                                        <li><a href="#">Quận Tây Hồ</a></li>
                                        <li><a href="#">Quận Thanh Xuân</a></li>
                                        <li><a href="#">Sơn Tây</a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>

                        <div className="title_new_item4">
                            <h6 style={{margin:"0",padding:"8px 0 8px 8px"}}>Tin tức mới nhất</h6>
                            {
                                ListWeather2.map((e,index)=>(
                                    <div className="row row_itemNew" style={{padding:"8px 8px"}}>
                                        <div className="col-4">
                                            <img width={"100%"} height={"60px"} src={e.img} alt="" />
                                        </div>
                                        <div className="col-8" style={{paddingLeft:"6px"}}>
                                            <p style={{fontSize:"12px"}}>{e.des}</p>
                                        </div>
                                    </div>
                                ))
                            }
                        </div>
                        
                    </div>
                </div>
            </div>
            <div className="tongquat">
                <div className="package_tongquat">
                    <p>Nội dung chính</p>
                    <p>1. Tổng quan về thủ đô Hà Nội</p>
                    <p>2. Thời tiết thành phố Hà Nội</p>
                    <small>2.1 Hà Nội có thời tiết không có sự khác biệt nhiều giữa các quận / huyện</small>
                    <small>2.2 Cung cấp thông tin dự báo thời tiết dài ngày</small>
                    <small>2.3 Dự báo thời tiết thành phố Hà Nội nhanh nhất</small>
                    <p>3. Danh sách các đơn vị hành chính thành phố Đà Nẵng</p>

                </div>
                <p className='des'>
                    Hà Nội là thủ đô của Việt Nam ta, toạ lạc ở trung tâm Bắc Bộ, thời tiết chia làm 2 mùa là mùa mưa và mùa khô rất rõ ràng, mùa đông khá lạnh và khô do chịu ảnh hưởng trực tiếp của những đợt không khí lạnh từ tháng 11 đến tháng 3 năm sau. Xem dự báo thời tiết Hà Nội giúp bạn nắm rõ được tình hình để chuẩn bị cho những dự định sắp đến.
                </p>
                <a className='link' href="#">Mở rộng</a>
            </div>
        </div>
    );
}

export default Weather;
