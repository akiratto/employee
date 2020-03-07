function F(field_name)
{
    return document.getElementById(field_name).value;
}

function F2(field_in_samegroup, field_name)
{
    var id_name = field_in_samegroup.id;
    var sections = id_name.split(':');
    sections[sections.length - 1] = field_name;
    var field_name2 = sections.join(':');
    return F(field_name2);
}

function F3(field_in_samegroup, field_name)
{
    var id_name = field_in_samegroup.id;
    var sections = id_name.split(':');
    var num = sections[2];
    return document.getElementById(field_name + "_" + num).firstChild.data;
}

