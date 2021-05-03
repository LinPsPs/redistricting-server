with open('MD/MD_plans.json','r') as f1:
    filedata = f1.read()
    filedata = filedata.replace('\\','')
    with open('MD/MD_plans_updated.json','w') as f2:
        f2.write(filedata)