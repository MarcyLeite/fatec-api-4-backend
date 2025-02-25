import semanticRelease from "semantic-release"
import { writeFileSync } from 'fs'
 
const getReleaseData = async () => {
    console.log({ dryRun: true, ci: false, branches: [process.env.GITHUB_REF] })
    const meta = await semanticRelease({ dryRun: true, ci: false, branches: ['refs/pull/5/merge'] })
    
    if(!meta) return
    return meta.nextRelease
}

const writeArtifacts = async () => {
    const data = await getReleaseData()
    if(!data) return

    writeFileSync('version', data.version)
    writeFileSync('notes', data.notes)
}

console.log(process.env)

console.log(process.env.GITHUB_REF)

writeArtifacts()