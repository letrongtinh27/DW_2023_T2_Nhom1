import React from 'react';
import Header from '../Header/Header';
import Footer from '../Footer/Footer';
import { Helmet } from 'react-helmet';

const MainLayout = ({children,title,description,keywords,author}) => {
    return (
        <div className='wrapper_MainLayout'>
            <Helmet>
                <meta charSet='utf-8' />
                <meta name='description' content={description} />
                <meta  name='keywords' content={keywords}/>
                <meta  name='author' content={author}/>
                <title>{title}</title>
            </Helmet>
            <Header/>
            <main style={{minHeight:'80vh',position:"relative",top:"234px",marginBottom:"50px"}}>
                {children}
            </main>
            <Footer/>
        </div>
    );
}

MainLayout.defaultProps = {
    title : 'Dự báo thời tiết',
    description : "My project",
    keywords : "reactJS, nodeJS, mongoodb",
    author : "Tên tác giả ..."
}


export default MainLayout;
