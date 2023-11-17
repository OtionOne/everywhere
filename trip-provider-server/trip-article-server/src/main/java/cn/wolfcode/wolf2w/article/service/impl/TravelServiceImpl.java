package cn.wolfcode.wolf2w.article.service.impl;

import cn.wolfcode.wolf2w.article.domain.Travel;
import cn.wolfcode.wolf2w.article.domain.TravelContent;
import cn.wolfcode.wolf2w.article.feign.IUserInfoFeignService;
import cn.wolfcode.wolf2w.article.mapper.TravelContentMapper;
import cn.wolfcode.wolf2w.article.mapper.TravelMapper;
import cn.wolfcode.wolf2w.article.query.TravelCondition;
import cn.wolfcode.wolf2w.article.query.TravelQuery;
import cn.wolfcode.wolf2w.article.service.ITravelService;
import cn.wolfcode.wolf2w.common.exception.LogicException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
* 游记服务接口实现
*/
@Service
@Transactional
public class TravelServiceImpl extends ServiceImpl<TravelMapper, Travel> implements ITravelService {

    @Autowired
    private TravelContentMapper travelContentMapper;

    @Autowired
    private IUserInfoFeignService userInfoFeignService;

    @Override
    public IPage<Travel> queryPage(TravelQuery qo) {
        IPage<Travel> page = new Page<>(qo.getCurrentPage(), qo.getPageSize());
        QueryWrapper<Travel> wrapper = Wrappers.<Travel>query();

        wrapper.like(StringUtils.hasText(qo.getKeyword()), "title", qo.getKeyword());

        wrapper.eq(qo.getDestId() != null, "dest_id", qo.getDestId());


        //旅行天数
        TravelCondition day = TravelCondition.DAY_MAP.get(qo.getDayType());
        if(day != null){
            wrapper.between("day", day.getMin(), day.getMax());
        }

        //人均消费
        TravelCondition avg = TravelCondition.CONSUME_MAP.get(qo.getConsumeType());
        if(avg != null){
            wrapper.between("avg_consume", avg.getMin(), avg.getMax());
        }

        //旅行时间
        TravelCondition time = TravelCondition.TIME_MAP.get(qo.getTravelTimeType());
        if(time != null){
            wrapper.between("month(travel_time)", time.getMin(), time.getMax());
        }
        super.page(page, wrapper);
        //游记用户
        for (Travel record : page.getRecords()) {
            record.setAuthor(userInfoFeignService.detail(record.getAuthorId()).getData());
        }
        return page;
    }

    @Override
    public TravelContent getContent(Long id) {
        return travelContentMapper.selectById(id);
    }

    @Override
    public void audit(Long id, int state) {
        //审核条件
        Travel travel = super.getById(id);
        if(travel == null || travel.getState() != Travel.STATE_WAITING){
            throw new LogicException("参数异常");
        }
        if(state == Travel.STATE_RELEASE){
            //审核通过
            //1：审核状态--通过
            travel.setState(Travel.STATE_RELEASE);
            //2：发布时间-当前时间
            travel.setReleaseTime(new Date());
            //3：最后更新时间-当前时间
            travel.setLastUpdateTime(new Date());
            //4：审核记录信息---审核人， 审核时间，审核备注，审核状态，审核记录id....  @TODO
            super.updateById(travel);
        }else{
            //审核拒绝
            //1：审核状态--拒绝
            travel.setState(Travel.STATE_REJECT);
            //2：发布时间-当前时间
            travel.setReleaseTime(null);
            //3：最后更新时间-当前时间
            travel.setLastUpdateTime(new Date());
            //4：审核记录信息---审核人， 审核时间，审核备注，审核状态，审核记录id....  @TODO
            super.updateById(travel);
        }
    }

    @Override
    public List<Travel> queryViewnumTop3(Long destId) {

        QueryWrapper<Travel> wrapper = new QueryWrapper<>();
        wrapper.eq("dest_id", destId);
        wrapper.orderByDesc("viewnum");
        wrapper.last(" limit 3");

        return super.list(wrapper);
    }

    @Override
    public List<Travel> queryByDestName(String name) {
        List<Travel> list = super.list(new QueryWrapper<Travel>().eq("dest_name", name));
        for (Travel travel : list) {
            travel.setAuthor(userInfoFeignService.detail(travel.getAuthorId()).getData());
        }
        return list;
    }

    @Override
    public void removeContent(Long id) {
        travelContentMapper.deleteById(id);
    }
}
