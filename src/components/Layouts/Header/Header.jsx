import React from 'react';
import './Header.css'
import Logo from '../../../Images/logo.png'
import dowm from '../../../Images/down.png'
import rain from '../../../Images/luongmua.png'
import khampha from '../../../Images/khampha.png'
import search from '../../../Images/search.png'
const Header = () => {
    return (
        <div className='wrapper_header'>
            <div className="logo">
                <img src={Logo} width={"100%"} alt="" />
            </div>
            <div className="header_tabs">
                <div className="row">
                    <div className="col" >
                        <ul className="nav">
                            <li className="nav-item">
                                <a className="nav-link" aria-current="page" href="#">Thành phố của bạn</a>
                            </li>
                            <li className="nav-item ">
                                <a className="nav-link active" href="#">Hà Nội</a>
                            </li>
                            <li className="nav-item">
                                <a className="nav-link" href="#">Thay đổi</a>
                            </li>
                        </ul>
                    </div>
                    <div className="col" style={{display:"flex",justifyContent:"center"}}>
                        <div className="tab_item">Giờ địa phương : 
                            <span className="tab_item_span">
                                16:00 | 19/10/2023
                            </span>
                        </div>
                    </div>
                </div>
                <div className="row">
                    <div className="col-7" style={{display:"flex",justifyContent:"end"}}>
                        <div className='search'>
                            <img src={search} alt="" className="search_logo" />
                            <input placeholder='Nhập tên địa điểm ...' type="text" className='search_input' />
                        </div>
                    </div>
                    <div className="col-5">
                        <ul className="nav" style={{justifyContent:"space-evenly"}}>
                            <li className="nav-item">
                                <a className="nav-link" aria-current="page" href="#">
                                    Tỉnh thành phố
                                    <img src={dowm} alt="" />
                                </a>
                            </li>
                            <li className="nav-item ">
                                <a className="nav-link" href="#">
                                    <img src={rain} alt="" />
                                    Lượng mưa</a>
                            </li>
                            <li className="nav-item">
                                <a className="nav-link" href="#">
                                    <img src={khampha} alt="" />
                                    Khám phá</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Header;
