export default function Logo({ large, sidebar }) {
  return (
    <img
      src="/logo.png"
      alt="JR Barbershop"
      className={`block h-auto object-contain ${large ? 'w-56' : sidebar ? 'w-52' : 'w-32'}`}
    />
  )
}
