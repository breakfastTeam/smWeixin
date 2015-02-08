<#setting date_format="yyyy-MM-dd">
<#setting datetime_format="yyyy-MM-dd HH:mm:ss">

<#function getTime time="">
    <#if time?is_date>
        <#return time?datetime>
    <#else>
        <#return time>
    </#if>
</#function>

<#function getDate time="">
    <#if time?is_date>
        <#return time?date>
    <#else>
        <#return time>
    </#if>
</#function>

<#macro select type="" list=[] name="name" id="id" value="" class="" style="width:120px" size="" extraProps=""
hasEmpty=false emptyKey="" emptyText="全部" disabled=false onchange="" onclick="" onfocus="" onblur="" hasAuth=false>
    <#if hasAuth>
        <#if type!="" && optionMap["${type}"]??>
            <#list optionMap["${type}"] as item>
                <#local emptyKey = emptyKey + ",'" +item.value + "'">
            </#list>
            <#if (emptyKey?length>0)>
                <#local emptyKey = emptyKey?substring(1)>
            </#if>
        <#elseif (0<list?size) >
            <#list list as item>
                <#local emptyKey = emptyKey +",'" +item.value + "'">
            </#list>
            <#if (emptyKey?length>0)>
                <#local emptyKey = emptyKey?substring(1)>
            </#if>
        </#if>
    </#if>
<select name="${name}" id="${id}" style="${style}" ${extraProps}
    <#rt>
    <#if size!="">
        size="${size}"
    </#if>
    <#rt>
    <#if class!="">
        class="${class}"
    </#if>
    <#rt>
    <#if onchange!="">
        onchange="${onchange}"
        <#rt>
    </#if>
    <#if onclick!="">
        onclick="${onclick}"
        <#rt>
    </#if>
    <#if onfocus!="">
        onfocus="${onfocus}"
        <#rt>
    </#if>
    <#if disabled>
        disabled
        <#rt>
    </#if>
        >
    <#if hasEmpty>
        <option value="${emptyKey?html}">${emptyText?html}</option>
    </#if>
    <#if type!="" && optionMap["${type}"]??>
        <#list optionMap["${type}"] as item>
            <option value="${item.value!}"
                <#rt>
                <#if value==item.value>
                    selected
                    <#rt>
                </#if>
                    >${item.text!}</option>
        </#list>
    <#elseif 0<list?size >
        <#list list as item>
            <option value="${item.value!}"
                <#rt>
                <#if item.value??&&value==item.value>
                    selected
                    <#rt>
                </#if>
                    >${item.text!}</option>
        </#list>
    </#if>
</select>
</#macro>

<#macro pageParam>
<input type='hidden' id="pageTotal" value='${page.getTotalElements()}'/>
<input type='hidden' id="totalPages" value='${page.getTotalPages()}'/>
<input type='hidden' id="page" value='${page.getNumber()}'/>
<input type='hidden' id="size" value='${page.getSize()}'/>
</#macro>


<#macro emptyPage page msg="暂无数据">
    <#if !page.hasContent()>
    <div class="alert alert-warning alert-dismissible" role="alert" style="margin-top: 20px;">
        <button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
        <strong>${msg}!</strong>
    </div>
    </#if>
</#macro>

<#macro stars count=0>
    <#list 0..(count-1) as t><i class="fa fa-star fa-fw" style="color:#f0ad4e;"></i></#list>
    <#if count<5>
    <#list 0..(4-count) as t><i class="fa fa-star-o fa-fw"></i></#list>
    </#if>
</#macro>

<#macro toggleText msg="">
    data-toggle="tooltip" data-original-title="${msg}"
</#macro>

